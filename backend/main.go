package main

import (
	"url-shortening-service/database"
	"url-shortening-service/server"
	//"github.com/aws/aws-lambda-go/lambda"
	"log"
)



func main() {
	log.Println("Connecting to DB")
	database.ConnectToDB()
	log.Println("Connected to DB")
	server.Run()
	//lambda.Start(server.Run)
}