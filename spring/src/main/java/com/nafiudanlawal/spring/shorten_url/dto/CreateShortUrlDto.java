package com.nafiudanlawal.spring.shorten_url.dto;


import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record CreateShortUrlDto(
        @NotNull(message = "url is required")
        @URL(message = "url must be a valid url")
        String url

) {
}
