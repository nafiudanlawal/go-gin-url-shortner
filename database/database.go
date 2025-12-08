package database

import (
	"log"
	"url-shortening-service/utils"
)

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