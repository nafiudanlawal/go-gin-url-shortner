package utils

import (
	"github.com/joho/godotenv"
	"log"
)

func GetEnvVars() map[string]string {
	var envVariables map[string]string
	envVariables, readErr := godotenv.Read()
	if readErr != nil {
		log.Fatal("Error reading .env file\n", readErr)
	}
	return envVariables
}

