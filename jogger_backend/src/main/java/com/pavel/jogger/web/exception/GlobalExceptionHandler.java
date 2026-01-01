package com.pavel.jogger.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized exception handler for the API.
 * <p>
 * Intercepts exceptions thrown by Controllers and converts them into
 * the standard {@link ApiError} format (Status + Map of Errors).
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    
    @ExceptionHandler(MethodArgumentNotValidException.class)

    /**
     * Handles validation errors (e.g. from @Valid annotations).
     * <p>
     * Extracts field-specific errors (e.g., "email" -> "invalid format")
     * and puts them into the error map. This allows the UI to highlight exactly
     * which input fields are wrong.
     * </p>
     */
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                errors
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles Not Found resources (404).
     * Wraps the simple message in a map with key "message".
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(
                new ApiError(HttpStatus.NOT_FOUND.value(), Map.of("message", ex.getMessage())),
                HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handles Access Denied (403).
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbidden(ForbiddenException ex) {
        return new ResponseEntity<>(
                new ApiError(HttpStatus.FORBIDDEN.value(), Map.of("message", ex.getMessage())),
                HttpStatus.FORBIDDEN
        );
    }

    /**
     * Handles Data Conflicts (409).
     * e.g., Duplicate email.
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex) {
        return new ResponseEntity<>(
                new ApiError(HttpStatus.CONFLICT.value(), Map.of("message", ex.getMessage())),
                HttpStatus.CONFLICT
        );
    }

    /**
     * Handles Bad Requests (400) for illegal arguments.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                new ApiError(HttpStatus.BAD_REQUEST.value(), Map.of("message", ex.getMessage())),
                HttpStatus.BAD_REQUEST
        );
    }
}
