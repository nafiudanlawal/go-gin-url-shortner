package com.nafiudanlawal.spring.shorten_url.dto;


import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public class CreateShortUrlDTO {
    @NotNull(message = "url is required")
    @URL(message = "url must be a valid url")
    private String url;

    public CreateShortUrlDTO(@NotNull(message = "url is required") String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
