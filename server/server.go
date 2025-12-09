package server

import (
	"fmt"
	"net/http"
	"time"
	"url-shortening-service/routes"
	"url-shortening-service/utils"

	"github.com/gin-gonic/gin"
)

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

	routes.AddRoutes(&Router.RouterGroup)

	s := &http.Server{
    Addr:           fmt.Sprintf("127.0.0.1:%s", utils.GetEnvVars()["PORT"]),
    Handler:        Router,
    ReadTimeout:    10 * time.Second,
    WriteTimeout:   10 * time.Second,
    MaxHeaderBytes: 1 << 20,
  }
  s.ListenAndServe()
  
}
