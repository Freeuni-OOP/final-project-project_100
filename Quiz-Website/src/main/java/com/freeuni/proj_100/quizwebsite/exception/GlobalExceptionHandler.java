package com.freeuni.proj_100.quizwebsite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Central interceptor for handling exceptions thrown across all REST controllers.
 * <p>
 * Utilizing {@link RestControllerAdvice}, this class automatically catches specified 
 * exceptions and serializes them into a consistent error response structure containing 
 * descriptive messages and execution timestamps.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Intercepts and processes custom application authentication failures.
     * Maps an {@link AuthException} to a structured JSON body with a 401 status.
     *
     * @param e the intercepted authentication exception containing the specific error cause
     * @return a {@link ResponseEntity} with HTTP status 401 (Unauthorized) containing the error details
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", e.getMessage(),
                        "timestamp", LocalDateTime.now().toString()
                ));
    }

    /**
     * Catches any unhandled generic exceptions that propagate up to the controller layer.
     * <p>
     * Sanitizes internal error messaging by hiding raw code tracebacks from the client 
     * and displaying a generic server message instead.
     * </p>
     *
     * @param e the generic unhandled root exception instance
     * @return a {@link ResponseEntity} with HTTP status 500 (Internal Server Error) containing a generic alert
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Something went wrong",
                        "timestamp", LocalDateTime.now().toString()
                ));
    }
}
