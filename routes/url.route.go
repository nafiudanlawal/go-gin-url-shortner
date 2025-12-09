package routes

import (
	"net/http"
	"github.com/gin-gonic/gin"
)

func AddRoutes(rg *gin.RouterGroup)  {
	shortUrlRoutes := rg.Group("shorten")
	
	shortUrlRoutes.GET("/shorten/:shortCode", func(c *gin.Context) {
		shortCode := c.Params.ByName("id")
		c.JSON(http.StatusOK, "shorten:" + shortCode )
	})

	shortUrlRoutes.GET("/shorten", func(c *gin.Context) {
		c.JSON(http.StatusOK, "shorten post")
	})

	shortUrlRoutes.POST("/shorten", func(c *gin.Context) {
		c.JSON(http.StatusOK, "shorten post")
	})

	shortUrlRoutes.PATCH("/shorten", func(c *gin.Context) {
		c.JSON(http.StatusOK, "shorten patch")
	})
}
