package service

import (
	"context"
	"url-shortening-service/database"
	"url-shortening-service/models"

	"gorm.io/gorm"
)

func CreateStat(shortenUrlID uint64, location, ip string) (result model.UrlAccessLog, err error) {
	// generate shortCode
	newLog := model.UrlAccessLog{ShortenUrlID: shortenUrlID, Location: location, OriginIp: ip}
	ctx := context.Background()
	err = gorm.G[model.UrlAccessLog](database.DbConnection).Create(ctx, &newLog)
	result = newLog
	return
}

func GetAllStat() (result []model.ShortenUrl, err error) {
	ctx := context.Background()
	return gorm.G[model.ShortenUrl](database.DbConnection).Find(ctx)
}

func CountStats(code string) (result int64, err error) {
	ctx := context.Background()
	return gorm.G[model.ShortenUrl](database.DbConnection).Where(&model.ShortenUrl{ShortCode: code}).Count(ctx, "1")
}
