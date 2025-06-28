package com.enterprise.ecm.shared.exception;

/**
 * Exception thrown when business rules are violated.
 */
public class BusinessException extends BaseException {
    
    public BusinessException(String message, String userMessage) {
        super(message, "BUSINESS_RULE_VIOLATION", userMessage);
    }
    
    public BusinessException(String message, String userMessage, Object... messageArgs) {
        super(message, "BUSINESS_RULE_VIOLATION", userMessage, messageArgs);
    }
    
    public BusinessException(String message, Throwable cause, String userMessage) {
        super(message, cause, "BUSINESS_RULE_VIOLATION", userMessage);
    }
    
    public BusinessException(String message, Throwable cause, String userMessage, Object... messageArgs) {
        super(message, cause, "BUSINESS_RULE_VIOLATION", userMessage, messageArgs);
    }
} 