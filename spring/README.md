# Url Shortner
Implementation of a url shortner

# Requirements
- Java 17
- *Docker - optional*


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
|-----------|---------------------------|
| GET   	| /shorten/                 | 
| POST 		| /shorten/                 |
| GET   	| /shorten/:shortCode       |
| GET    	| /shorten/:shortCode/stats |
| PUT    	| /shorten/:shortCode       |
| DELETE 	| /shorten/:shortCode       |
| GET    	| /:{shortCode}				|