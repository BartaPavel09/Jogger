package com.pavel.jogger.web.exception;

/**
 * Exception thrown when a data integrity conflict occurs.
 * <p>
 * Maps to HTTP 409 Conflict.
 * Typical use case: User tries to register with an email that is already taken.
 * </p>
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
