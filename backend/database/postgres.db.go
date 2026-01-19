package database

import (
	"fmt"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
	"log"
	"os"
	"time"
	"url-shortening-service/utils"
)

func connectToPostgresDB() *gorm.DB {
	var host, user, password, dbName, port = GetPostgresDbCredentials()
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
	
	dsn := fmt.Sprintf("host=%s user=%s password=%s port=%s sslmode=prefer",
		host,
		user,
		password,
		port,
	)
	database, err := gorm.Open(postgres.Open(dsn), &gorm.Config{Logger: newLogger})

	if err != nil {
		log.Println(err.Error())
		log.Fatal("failed to connect database server\n")
		os.Exit(-1)
	}

	_ = database.Exec(fmt.Sprintf("CREATE DATABASE %s;", dbName))

	dsn = fmt.Sprintf("host=%s user=%s password=%s port=%s dbname=%s sslmode=prefer",
		host,
		user,
		password,
		port,
		dbName,
	)
	dbInstance, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Println(err.Error())
		log.Fatal("failed to connect database\n")
		os.Exit(-1)
	}
	return dbInstance
}

func GetPostgresDbCredentials() (host, user, password, dbName, port string) {
	host = utils.GetEnv("DB_HOST")
	user = utils.GetEnv("DB_USER")
	password = utils.GetEnv("DB_PASSWORD")
	dbName = utils.GetEnv("DB_NAME")
	port = utils.GetEnv("DB_PORT")
	return
}
