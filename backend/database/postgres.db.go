package database

import (
	"fmt"
	"log"
	
	"gorm.io/driver/postgres"
	"url-shortening-service/utils"
	"gorm.io/gorm"
	
)

func connectToPostgresDB() *gorm.DB {
	var host, username, password, dbName, port = GetPostgresDbParameters()
	dsn := fmt.Sprintf("host=%s user=%s password=%s port=%s sslmode=disable",
		host,
		username,
		password,
		port,
	)
	database, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})

	if err != nil {
		log.Fatal("failed to connect database server\n")
	}

	_ = database.Exec(fmt.Sprintf("CREATE DATABASE %s;", dbName))
	dsn = fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=disable",
		host,
		username,
		password,
		dbName,
		port,
	)
	dbInstance, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatal("failed to connect database\n")
	}
	return dbInstance
}

func GetPostgresDbParameters() (host, username, password, dbName, port string) {
	exist := true
	var envVariables = utils.GetEnvVars()
	host, exist = envVariables["DB_HOST"]
	if !exist {
		log.Fatal("DB_HOST not in .env")
	}
	dbName, exist = envVariables["DB_NAME"]
	if !exist {
		log.Fatal("DB_NAME not in .env")
	}
	username, exist = envVariables["DB_USERNAME"]
	if !exist {
		log.Fatal("DB_USERNAME not in .env")
	}
	password, exist = envVariables["DB_PASSWORD"]
	if !exist {
		log.Fatal("DB_PASSWORD not in .env")
	}
	port, exist = envVariables["DB_PORT"]
	if !exist {
		log.Fatal("DB_PORT not in .env")
	}
	return

}
