package model

import "gorm.io/gorm"

type ShortenUrl struct{
	gorm.Model
	ID         		uint64 `gorm:"primaryKey"`
	Url 			string `gorm:"not null;length=128;uniqueIndex;size:128"`
	ShortCode 		string `gorm:"not null;length=15;uniqueIndex;size:15"`
	UrlAccessLogs 	[]UrlAccessLog `gorm:"constraint:OnUpdate:CASCADE,OnDelete:CASCADE;"`
}