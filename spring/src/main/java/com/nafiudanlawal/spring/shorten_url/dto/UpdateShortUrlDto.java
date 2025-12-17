package com.nafiudanlawal.spring.shorten_url.dto;

import org.hibernate.validator.constraints.URL;

public record UpdateShortUrlDto(
        @URL(message = "url must be a valid url")
        String url
) {
}
