package com.enterprise.ecm.shared.exception;

import lombok.Getter;

/**
 * Base exception class for all custom exceptions in the ECM system.
 * Provides common functionality for error handling and logging.
 */
@Getter
public abstract class BaseException extends RuntimeException {
    
    private final String errorCode;
    private final String userMessage;
    private final Object[] messageArgs;
    
    protected BaseException(String message, String errorCode, String userMessage, Object... messageArgs) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
        this.messageArgs = messageArgs;
    }
    
    protected BaseException(String message, Throwable cause, String errorCode, String userMessage, Object... messageArgs) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
        this.messageArgs = messageArgs;
    }
    
    /**
     * Get the formatted user message with arguments substituted.
     */
    public String getFormattedUserMessage() {
        if (messageArgs == null || messageArgs.length == 0) {
            return userMessage;
        }
        return String.format(userMessage, messageArgs);
    }
} 