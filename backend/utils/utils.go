package utils

import (
	"log"
	"os"
	"github.com/joho/godotenv"
)

func GetEnvVars() map[string]string {
	envVariables, readErr := godotenv.Read()
	if readErr != nil {
		log.Fatalln("Error reading .env file for envs", readErr)
	}
	return envVariables
}

func GetFileEnv(key string) string {
	envVariables, readErr := godotenv.Read()
	if readErr != nil {
		log.Fatalln("Error reading .env file", readErr)
	}
	envVariable, exist := envVariables[key]
	if !exist {
		log.Fatalf("%s not in .env", key)
	}
	return envVariable
}

func GetEnv(key string) string {
	envVariable, exist := os.LookupEnv(key)
	if !exist {
		return GetFileEnv(key)
	}
	return envVariable
}
