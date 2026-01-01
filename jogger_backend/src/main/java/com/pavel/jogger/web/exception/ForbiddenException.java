package com.pavel.jogger.web.exception;

/**
 * Exception thrown when access is denied due to insufficient permissions.
 * <p>
 * Maps to HTTP 403 Forbidden.
 * Distinct from 401 Unauthorized (which means "I don't know who you are").
 * 403 means "I know who you are, but you can't touch this".
 * </p>
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
