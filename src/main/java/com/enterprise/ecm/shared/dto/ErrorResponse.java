package com.enterprise.ecm.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Standardized error response for API endpoints.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private LocalDateTime timestamp;
    private String errorCode;
    private String message;
    private String userMessage;
    private String path;
    private String method;
    private String traceId;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, List<String>> fieldErrors;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> globalErrors;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String details;
} 