package server

import (
	"fmt"
	"net/http"
	"time"
	"url-shortening-service/utils"
	"github.com/gin-gonic/gin"
)

func Run(){
	router := gin.New()
	router.Use(gin.LoggerWithFormatter(func(param gin.LogFormatterParams) string {
		// custom format
		return fmt.Sprintf("%s | %10s |%s%-5s%s| %s%5d%s | %s |  %8s | %s | %s | %s\n",
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
	router.GET("/ping", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"message": "pong",
		})
	})

	fmt.Printf(":%s", utils.GetEnvVars()["PORT"])

	s := &http.Server{
    Addr:           fmt.Sprintf(":%s", utils.GetEnvVars()["PORT"]),
    Handler:        router,
    ReadTimeout:    10 * time.Second,
    WriteTimeout:   10 * time.Second,
    MaxHeaderBytes: 1 << 20,
  }
  s.ListenAndServe()
  
}
