package server

import (
	"fmt"
	"log"
	"net/http"
	"time"
	"url-shortening-service/routes"
	"url-shortening-service/utils"

	"github.com/gin-gonic/gin"
)

func Run() {
	router := gin.New()
	router.Use(gin.LoggerWithFormatter(func(param gin.LogFormatterParams) string {
		// custom format
		return fmt.Sprintf("%s | %10s |%s%-5s%s| %s%5d%s | %8s |  %18s | %s | %s | %s\n",
			param.TimeStamp.Format(time.RFC3339),
			param.ClientIP,
			param.MethodColor(),
			param.Method,
			param.ResetColor(),
			param.StatusCodeColor(),
			param.StatusCode,
			param.ResetColor(),
			param.Latency,
			param.Path,
			param.Request.UserAgent(),
			param.Request.Proto,
			param.ErrorMessage,
		)
	}))
	router.Use(gin.Recovery())
	router.LoadHTMLFiles("static/index.tmpl", "static/not-found.tmpl")
	router.Static("/static", "./static")

	routes.AddRoutes(&router.RouterGroup)

	router.NoRoute(func(c *gin.Context) {
		c.HTML(http.StatusOK, "not-found.tmpl", gin.H{})
	})
	router.GET("/:code", func(c *gin.Context) {
		code := c.Params.ByName("code")
		c.HTML(http.StatusOK, "index.tmpl", gin.H{
			"code": code,
		})
	})
	port := utils.GetEnv("PORT")
	s := &http.Server{
		Addr:           fmt.Sprintf("127.0.0.1:%s", port),
		Handler:        router,
		ReadTimeout:    10 * time.Second,
		WriteTimeout:   10 * time.Second,
		MaxHeaderBytes: 1 << 20,
	}
	log.Printf("Server running on port: %s\n", port)
	s.ListenAndServe()

}
