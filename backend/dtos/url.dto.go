package dtos

type CreateUrl struct {
	Url string `json:"url" binding:"required,url"`
}

type UpdateUrl struct {
	Url string `json:"url" binding:"required,url"`
}