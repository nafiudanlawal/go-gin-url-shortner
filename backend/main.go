package main

import (
	"url-shortening-service/database"
	"url-shortening-service/server"
)

func main() {
	database.ConnectToDB()
	server.Run()
}