package com.pavel.jogger.web.exception;

import java.util.Map;

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
