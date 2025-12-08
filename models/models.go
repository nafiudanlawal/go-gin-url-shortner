package model

func GetDBModels() []interface{}{
	return []interface{} {
		&ShortenUrl{},
		&UrlAccessLog{},
	}
}

