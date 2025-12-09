package service

import (
	"context"
	"crypto/sha1"
	"url-shortening-service/database"
	"url-shortening-service/models"

	"gorm.io/gorm"
)

func CreateUrl(url string) {

	// generate shortCode
	h := sha1.New()
	h.Write([]byte(url))
	code := string(h.Sum(nil)[:5])

	ctx := context.Background()
	gorm.G[model.ShortenUrl](database.DbConnection).Create(ctx, &model.ShortenUrl{Url: url, ShortCode: code})
}

func GetAll() (result []model.ShortenUrl, err error) {
	ctx := context.Background()
	return gorm.G[model.ShortenUrl](database.DbConnection).Find(ctx)
}

func GetByShortenUrl(code string) (result model.ShortenUrl, err error) {
	ctx := context.Background()
	return gorm.G[model.ShortenUrl](database.DbConnection).Where("shortCode = ?", code).First(ctx)
}

func GetByLongUrl(url string) (result model.ShortenUrl, err error) {
	ctx := context.Background()
	return gorm.G[model.ShortenUrl](database.DbConnection).Where("url = ?", url).First(ctx)
}
