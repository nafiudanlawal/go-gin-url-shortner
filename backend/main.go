package main

import (
	"url-shortening-service/database"
	"url-shortening-service/server"
	"log"
)

func main() {
	log.Println("Connecting to DB")
	database.ConnectToDB()
	log.Println("Connected to DB")
	server.Run()
}