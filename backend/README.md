# Url Shortner Service

## Requirements
### Environment
- Go (version 1.24.0+)
#### Go Packages
- [Gin](https://gin-gonic.com/en/docs/)
- [Gorm](https://gorm.io/)

## Run Application
### Docker
Build image
```bash
docker build -t url-shortner-service .  
```
Run service
```bash
docker compose up
```

## API
| Method 	| Endpoint 					|
| -------- 	| -------- 					|
| GET   	| /shorten/                  
| POST 		|/shorten/                 	|
| GET   	|/shorten/:shortCode       	|
| GET    	|/shorten/:shortCode/stats 	|
| PUT    	|/shorten/:shortCode       	|
| DELETE 	|/shorten/:shortCode       	|
| GET    	|/:{shortCode}				|