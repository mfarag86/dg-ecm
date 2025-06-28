package com.enterprise.ecm.shared.exception;

import java.util.List;
import java.util.Map;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends BaseException {
    
    private final Map<String, List<String>> fieldErrors;
    
    public ValidationException(String message, Map<String, List<String>> fieldErrors) {
        super(message, "VALIDATION_ERROR", "Validation failed. Please check your input.");
        this.fieldErrors = fieldErrors;
    }
    
    public ValidationException(String message, String userMessage, Map<String, List<String>> fieldErrors) {
        super(message, "VALIDATION_ERROR", userMessage);
        this.fieldErrors = fieldErrors;
    }
    
    public ValidationException(String message, String userMessage) {
        super(message, "VALIDATION_ERROR", userMessage);
        this.fieldErrors = null;
    }
    
    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
    
    public boolean hasFieldErrors() {
        return fieldErrors != null && !fieldErrors.isEmpty();
    }
} 