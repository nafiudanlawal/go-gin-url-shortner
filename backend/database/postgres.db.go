package database

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/aws/aws-sdk-go-v2/aws"
	"github.com/aws/aws-sdk-go-v2/config"
	"github.com/aws/aws-sdk-go-v2/service/secretsmanager"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
	"log"
	"os"
	"time"
	"url-shortening-service/utils"
)

type DBCredential struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

func connectToPostgresDB() *gorm.DB {
	var host, dbName, port = GetPostgresDbCredentials()
	newLogger := logger.New(
		log.New(os.Stdout, "\r\n", log.LstdFlags), // io writer
		logger.Config{
			SlowThreshold:             time.Second,   // Slow SQL threshold
			LogLevel:                  logger.Silent, // Log level
			IgnoreRecordNotFoundError: true,          // Ignore ErrRecordNotFound error for logger
			ParameterizedQueries:      true,
			Colorful:                  true, // Disable color
		},
	)
	credential := getAwsDbSecret()
	dsn := fmt.Sprintf("host=%s user=%s password=%s port=%s sslmode=prefer",
		host,
		credential.Username,
		credential.Password,
		port,
	)
	database, err := gorm.Open(postgres.Open(dsn), &gorm.Config{Logger: newLogger})

	if err != nil {
		log.Println(err.Error())
		log.Fatal("failed to connect database server\n")
	}

	_ = database.Exec(fmt.Sprintf("CREATE DATABASE %s;", dbName))

	dsn = fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=prefer",
		host,
		credential.Username,
		credential.Password,
		dbName,
		port,
	)
	dbInstance, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Println(err.Error())
		log.Fatal("failed to connect database\n")
	}
	return dbInstance
}

func GetPostgresDbCredentials() (host, dbName, port string) {
	host = utils.GetEnv("DB_HOST")
	dbName = utils.GetEnv("DB_NAME")
	port = utils.GetEnv("DB_PORT")
	return
}

func getAwsDbSecret() (dBCredential DBCredential) {
	region := utils.GetEnv("AWS_REGION")
	secretName := utils.GetEnv("DB_CREDENTIAL_SECRET")

	config, err := config.LoadDefaultConfig(context.Background(), config.WithRegion(region))
	if err != nil {
		log.Fatal(err.Error())
	}

	// Create Secrets Manager client
	svc := secretsmanager.NewFromConfig(config)

	input := &secretsmanager.GetSecretValueInput{
		SecretId: aws.String(secretName),
	}

	result, err := svc.GetSecretValue(context.Background(), input)
	if err != nil {
		// For a list of exceptions thrown, see
		// https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
		log.Fatal(err.Error())
	}

	// Decrypts secret using the associated KMS key.
	var secretString string = *result.SecretString

	err = json.Unmarshal([]byte(secretString), &dBCredential)
	if err != nil {
		log.Fatal(err.Error())
	}
	return
}
