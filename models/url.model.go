package model

import "gorm.io/gorm"

type ShortenUrl struct{
	gorm.Model
	Url string
	ShortCode string
	Accesslogs []UrlAccessLog `gorm:"foreignKey:ShortenUrlID;constraint:OnUpdate:CASCADE,OnDelete:CASCADE;"`
}