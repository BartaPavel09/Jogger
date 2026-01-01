package com.pavel.jogger.web.exception;

import java.util.Map;

/**
 * Custom error response structure used across the entire API.
 * <p>
 * Instead of a simple message, this class holds a Map of errors.
 * This is perfect for form validation where multiple fields might fail at once.
 * <br>
 * <b>JSON Example:</b>
 * <pre>
 * {
 * "status": 400,
 * "errors": {
 * "email": "Invalid email format",
 * "password": "Password must be 6+ chars"
 * }
 * }
 * </pre>
 * </p>
 */
public class ApiError {

    private int status;
    private Map<String, String> errors;

    public ApiError(int status, Map<String, String> errors) {
        this.status = status;
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
