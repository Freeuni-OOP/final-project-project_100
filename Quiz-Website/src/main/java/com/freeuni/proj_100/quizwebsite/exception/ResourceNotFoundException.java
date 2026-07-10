package com.freeuni.proj_100.quizwebsite.exception;

/**
 * Custom runtime exception thrown when a requested system resource cannot be found.
 * <p>
 * This exception is typically thrown by service layers (e.g., when a quiz or user 
 * lookup by ID fails) and is intercepted by a global exception handler to return 
 * an HTTP 404 (Not Found) status code to the client.
 * </p>
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code ResourceNotFoundException} with the specified detail message.
     *
     * @param message the detail message explaining which resource was missing
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
