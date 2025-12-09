package model

import "gorm.io/gorm"

type ShortenUrl struct{
	gorm.Model
	Url string `gorm:"length=128;uniqueIndex"`
	ShortCode string `gorm:"length:8,uniqueIndex"`
	Accesslogs []UrlAccessLog `gorm:"foreignKey:ShortenUrlID;constraint:OnUpdate:CASCADE,OnDelete:CASCADE;"`
}