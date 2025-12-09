package routes

import (
	"net/http"
	"url-shortening-service/dtos"
	"url-shortening-service/service"

	"github.com/gin-gonic/gin"
)

func AddRoutes(rg *gin.RouterGroup) {
	shortUrlRoutes := rg.Group("shorten")

	shortUrlRoutes.GET("/", func(c *gin.Context) {
		result, err := service.GetAllUrls()
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"message": err})
			return
		}
		c.JSON(http.StatusOK, result)
	})

	shortUrlRoutes.POST("/", func(c *gin.Context) {
		var data dtos.CreateUrl
		// validate input
		if err := c.ShouldBindJSON(&data); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}
		result, err := service.CreateUrl(data.Url)
		if err != nil {
			c.JSON(http.StatusInternalServerError, err)
			return
		}
		c.JSON(http.StatusCreated, result)
	})

	shortUrlRoutes.GET("/:shortCode", func(c *gin.Context) {
		shortCode := c.Params.ByName("shortCode")
		result, err := service.GetUrlByShortenUrl(shortCode)
		if err != nil {
			c.JSON(http.StatusNotFound, gin.H{})
			return
		}
		_, err = service.CreateStat(result.ID, "unknown", c.Request.RemoteAddr)
		if err != nil {
			c.JSON(http.StatusInternalServerError, err)
			return
		}
		//delete(result, "UrlAccessLogs")
		c.JSON(http.StatusOK, result)
	})

	shortUrlRoutes.GET("/:shortCode/stats", func(c *gin.Context) {
		shortCode := c.Params.ByName("shortCode")
		result, err := service.GetUrlStatsByShortCode(shortCode)
		if err != nil {
			c.JSON(http.StatusNotFound, gin.H{})
			return
		}
		c.JSON(http.StatusOK, result)
	})

	shortUrlRoutes.PUT("/:shortCode", func(c *gin.Context) {
		shortCode := c.Params.ByName("shortCode")
		var data dtos.UpdateUrl
		// validate input
		if err := c.ShouldBindJSON(&data); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		result, err := service.UpdateUrlByShortenUrl(shortCode, data.Url)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{})
			return
		}
		c.JSON(http.StatusOK, result)
	})

	shortUrlRoutes.DELETE("/:shortCode", func(c *gin.Context) {
		shortCode := c.Params.ByName("shortCode")
		result, err := service.DeleteUrlByShortenUrl(shortCode)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{})
			return
		}
		c.JSON(http.StatusOK, result)
	})
}
