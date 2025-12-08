package model

import "gorm.io/gorm"

type AccessLog struct{
	gorm.Model
	ShortenUrlID uint // used as gorm foreign key
	Location string
	OriginIp string
}
