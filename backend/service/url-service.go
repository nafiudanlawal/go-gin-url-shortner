package service

import (
	"context"
	"math/rand/v2"
	"crypto/sha1"
	"encoding/hex"
	"url-shortening-service/database"
	"url-shortening-service/models"

	"gorm.io/gorm"
)

func CreateUrl(url string) (result model.ShortenUrl, err error) {
	// generate random shortCode
	h := sha1.New()
	h.Write([]byte(url))
	code := hex.EncodeToString(h.Sum(nil))
	start := rand.IntN(len(code) - 6)
	code = code[start: start + 5]

	// create record
	newUrl := model.ShortenUrl{Url: url, ShortCode: code}
	ctx := context.Background()
	err = gorm.G[model.ShortenUrl](database.DbConnection).Create(ctx, &newUrl)
	result = newUrl
	return
}

func GetAllUrls() (result []model.ShortenUrl, err error) {
	var shortenUrls []model.ShortenUrl
	err = database.DbConnection.Model(&model.ShortenUrl{}).Find(&shortenUrls).Error
	return shortenUrls, err
}

func GetUrlByShortenUrl(code string) (result model.ShortenUrl, err error) {
	ctx := context.Background()
	return gorm.G[model.ShortenUrl](database.DbConnection).Where(&model.ShortenUrl{ShortCode: code}).First(ctx)
}

func GetUrlById(id uint64) (result model.ShortenUrl, err error) {
	var shortenUrls []model.ShortenUrl
	err = database.DbConnection.Model(&model.ShortenUrl{}).Where("id = ?", id).Find(&shortenUrls).Error
	if err == nil {
		return shortenUrls[0], err
	}
	return
}
func GetUrlStatsByShortCode(code string) (result model.ShortenUrl, err error) {
	var shortenUrls []model.ShortenUrl
	err = database.DbConnection.Model(&model.ShortenUrl{}).Preload("UrlAccessLogs").Where(&model.ShortenUrl{ShortCode: code}).Find(&shortenUrls).Error
	if err == nil {
		return shortenUrls[0], err
	}
	return
}
func DeleteUrlByShortenUrl(code string) (result int, err error) {
	ctx := context.Background()
	return gorm.G[model.ShortenUrl](database.DbConnection).Where(&model.ShortenUrl{ShortCode: code}).Delete(ctx)
}

func UpdateUrlByShortenUrl(code string, url string) (result int, err error) {
	ctx := context.Background()
	return gorm.G[model.ShortenUrl](database.DbConnection).Where(&model.ShortenUrl{ShortCode: code}).Updates(ctx, model.ShortenUrl{Url: url})
}

func GetUrlByLongUrl(url string) (result model.ShortenUrl, err error) {
	ctx := context.Background()
	return gorm.G[model.ShortenUrl](database.DbConnection).Where(&model.ShortenUrl{Url: url}).First(ctx)
}
