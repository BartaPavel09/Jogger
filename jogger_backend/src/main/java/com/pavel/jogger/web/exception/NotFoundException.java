package com.pavel.jogger.web.exception;

/**
 * Exception thrown when a requested resource is not found in the database.
 * <p>
 * Maps directly to HTTP 404 Not Found.
 * Usage: throw new NotFoundException("Runner not found with id: 5");
 * </p>
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
