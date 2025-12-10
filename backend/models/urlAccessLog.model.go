package model

import "gorm.io/gorm"

type UrlAccessLog struct{
	gorm.Model
	ID         		uint64 `gorm:"primaryKey"`
	ShortenUrlID 	uint64 `gorm:"not null"`
	Location 		string `gorm:"size:256"`
	OriginIp 		string `gorm:"size:256"`
}
