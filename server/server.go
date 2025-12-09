package server

import (
	"fmt"
	"net/http"
	"time"
	"url-shortening-service/utils"
	"github.com/gin-gonic/gin"
)
var Router *gin.Engine
func Run(){
	Router := gin.New()
	Router.Use(gin.LoggerWithFormatter(func(param gin.LogFormatterParams) string {
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
	Router.Use(gin.Recovery())
	Router.GET("/ping", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"message": "pong",
		})
	})

	fmt.Printf(":%s", utils.GetEnvVars()["PORT"])

	s := &http.Server{
    Addr:           fmt.Sprintf(":%s", utils.GetEnvVars()["PORT"]),
    Handler:        Router,
    ReadTimeout:    10 * time.Second,
    WriteTimeout:   10 * time.Second,
    MaxHeaderBytes: 1 << 20,
  }
  s.ListenAndServe()
  
}
