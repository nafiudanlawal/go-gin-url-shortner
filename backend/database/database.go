package database

import (
	"url-shortening-service/models"
	"gorm.io/gorm"
)

var DbConnection *gorm.DB


func ConnectToDB(){
	dbInstance := connectToPostgresDB()
	dbInstance.AutoMigrate(&model.ShortenUrl{}, &model.UrlAccessLog{})
	DbConnection = dbInstance
}