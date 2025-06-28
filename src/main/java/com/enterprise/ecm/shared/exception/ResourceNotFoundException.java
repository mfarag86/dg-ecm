package com.enterprise.ecm.shared.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends BaseException {
    
    public ResourceNotFoundException(String resourceType, String identifier, Object value) {
        super(
            String.format("%s not found with %s: %s", resourceType, identifier, value),
            "RESOURCE_NOT_FOUND",
            "The requested %s was not found",
            resourceType
        );
    }
    
    public ResourceNotFoundException(String resourceType, String identifier, Object value, Throwable cause) {
        super(
            String.format("%s not found with %s: %s", resourceType, identifier, value),
            cause,
            "RESOURCE_NOT_FOUND",
            "The requested %s was not found",
            resourceType
        );
    }
    
    public ResourceNotFoundException(String message, String userMessage) {
        super(message, "RESOURCE_NOT_FOUND", userMessage);
    }
} 