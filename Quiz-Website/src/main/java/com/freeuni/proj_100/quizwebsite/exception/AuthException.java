package com.freeuni.proj_100.quizwebsite.exception;

/**
 * Custom runtime exception thrown when an authentication or authorization failure occurs.
 *
 */
public class AuthException extends RuntimeException {
    /**
     * Constructs a new {@code AuthException} with the specified detailed error message.
     *
     * @param message the detailed error message explaining the cause of the authentication failure
     */
    public AuthException(String message) {
        super(message);
    }
}