package com.nafiudanlawal.spring.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ControllerExceptionHandler {
    @ExceptionHandler(value = NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponseDto notFound(NoSuchElementException ex) {
        return new ApiErrorResponseDto(ex.getMessage(),HttpStatus.NOT_FOUND.value(), new Date());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiErrorResponseDto handlePostgresExceptions(
            DataIntegrityViolationException ex) {
        Throwable root = ex.getRootCause();
        String message;
        if (root instanceof SQLException sqlEx) {
            String sqlState = sqlEx.getSQLState();

            message = switch (sqlState) {
                case "23505" -> "Resource already exists";        // unique_violation
                case "23503" -> "Referenced resource not found"; // foreign_key_violation
                case "23502" -> "Required field is missing";     // not_null_violation
                default -> "Database constraint violation";
            };
            return new ApiErrorResponseDto(message,HttpStatus.CONFLICT.value(), new Date());
        }
        return new ApiErrorResponseDto(ex.getCause().getMessage(),HttpStatus.BAD_REQUEST.value(), new Date());
    }

}
