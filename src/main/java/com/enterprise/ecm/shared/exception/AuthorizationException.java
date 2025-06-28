package com.enterprise.ecm.shared.exception;

/**
 * Exception thrown when authorization fails.
 */
public class AuthorizationException extends BaseException {
    
    public AuthorizationException(String message) {
        super(message, "AUTHORIZATION_FAILED", "You don't have permission to perform this action.");
    }
    
    public AuthorizationException(String message, String userMessage) {
        super(message, "AUTHORIZATION_FAILED", userMessage);
    }
    
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause, "AUTHORIZATION_FAILED", "You don't have permission to perform this action.");
    }
    
    public AuthorizationException(String message, Throwable cause, String userMessage) {
        super(message, cause, "AUTHORIZATION_FAILED", userMessage);
    }
} 