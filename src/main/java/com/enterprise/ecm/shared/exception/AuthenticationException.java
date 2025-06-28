package com.enterprise.ecm.shared.exception;

/**
 * Exception thrown when authentication fails.
 */
public class AuthenticationException extends BaseException {
    
    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_FAILED", "Authentication failed. Please check your credentials.");
    }
    
    public AuthenticationException(String message, String userMessage) {
        super(message, "AUTHENTICATION_FAILED", userMessage);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause, "AUTHENTICATION_FAILED", "Authentication failed. Please check your credentials.");
    }
    
    public AuthenticationException(String message, Throwable cause, String userMessage) {
        super(message, cause, "AUTHENTICATION_FAILED", userMessage);
    }
} 