package com.nafiudanlawal.spring.shorten_url.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

public record CreateShortUrlDto(
        @NotNull(message = "required")
        @NotBlank(message = "must not be blank")
        @URL(message = "must be a valid url")
        String url

) {
}
