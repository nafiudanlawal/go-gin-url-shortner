package com.nafiudanlawal.spring.shorten_url.dto;

import java.util.Date;

public record AccessLogResponseDto(
        Integer id,
        String shortCode,
        String url,
        Date createAt,
        Date updateAt,

        Integer accessCount
) {
}
