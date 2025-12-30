package com.nafiudanlawal.spring.exception;

import java.util.Date;

public record ApiErrorResponseDto(
        String message,
        int status,
        Date timestamp
) {
}
