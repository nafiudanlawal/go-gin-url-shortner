package database

import (
	"fmt"
	"log"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

var DbConnection *gorm.DB

func ConnectToPostgresDB(){
  var host, username, password, dbName, port = GetPostgresDbParameters()

  dsn := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=disable", 
    host, 
    username, 
    password, 
    dbName, 
    port,
  )

  db, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
  if err != nil {
    log.Fatal("failed to connect database\n", err)
  }
  DbConnection = db
}
