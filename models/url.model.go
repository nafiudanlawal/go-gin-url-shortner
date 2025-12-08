package model

import "gorm.io/gorm"

type ShortenUrl struct{
	gorm.Model
	Url string
	ShortCode string
	Accesslogs []AccessLog `gorm:"foreignKey:ShortenUrlID;constraint:OnUpdate:CASCADE,OnDelete:CASCADE;"`
}