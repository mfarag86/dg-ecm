package com.enterprise.ecm.shared.exception;

import com.enterprise.ecm.shared.dto.ErrorResponse;
import com.enterprise.ecm.shared.logging.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Global exception handler for all controllers.
 * Provides centralized exception handling and logging.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    
    private final LoggingService loggingService;
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = buildErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getFormattedUserMessage(),
                request,
                null
        );
        
        loggingService.logWarn("Resource not found: {}", ex.getMessage(), 
                Map.of("path", request.getRequestURI(), "method", request.getMethod()));
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, HttpServletRequest request) {
        
        ErrorResponse.ErrorResponseBuilder errorBuilder = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .userMessage(ex.getFormattedUserMessage())
                .path(request.getRequestURI())
                .method(request.getMethod());
        
        if (ex.hasFieldErrors()) {
            errorBuilder.fieldErrors(ex.getFieldErrors());
        }
        
        loggingService.logWarn("Validation error: {}", ex.getMessage(),
                Map.of("path", request.getRequestURI(), "fieldErrors", ex.getFieldErrors()));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBuilder.build());
    }
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = buildErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getFormattedUserMessage(),
                request,
                null
        );
        
        loggingService.logWarn("Business rule violation: {}", ex.getMessage(),
                Map.of("path", request.getRequestURI(), "method", request.getMethod()));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(
                "AUTHENTICATION_FAILED",
                ex.getMessage(),
                "Authentication failed. Please check your credentials.",
                request,
                null
        );
        loggingService.logWarn("Authentication failed: {}", ex.getMessage(),
                Map.of("path", request.getRequestURI(), "method", request.getMethod()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(
            AuthorizationException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = buildErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getFormattedUserMessage(),
                request,
                null
        );
        
        loggingService.logWarn("Authorization failed: {}", ex.getMessage(),
                Map.of("path", request.getRequestURI(), "method", request.getMethod()));
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        Map<String, java.util.List<String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .errorCode("VALIDATION_ERROR")
                .message("Validation failed")
                .userMessage("Please check your input and try again")
                .path(request.getRequestURI())
                .method(request.getMethod())
                .fieldErrors(fieldErrors)
                .build();
        
        loggingService.logWarn("Method argument validation failed: {}", ex.getMessage(),
                Map.of("path", request.getRequestURI(), "fieldErrors", fieldErrors));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        
        Map<String, java.util.List<String>> fieldErrors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.groupingBy(
                        violation -> violation.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                ));
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .errorCode("VALIDATION_ERROR")
                .message("Validation failed")
                .userMessage("Please check your input and try again")
                .path(request.getRequestURI())
                .method(request.getMethod())
                .fieldErrors(fieldErrors)
                .build();
        
        loggingService.logWarn("Constraint validation failed: {}", ex.getMessage(),
                Map.of("path", request.getRequestURI(), "fieldErrors", fieldErrors));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = buildErrorResponse(
                "AUTHENTICATION_FAILED",
                "Invalid credentials",
                "Invalid username or password",
                request,
                null
        );
        
        loggingService.logWarn("Bad credentials for request: {}", request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = buildErrorResponse(
                "AUTHORIZATION_FAILED",
                "Access denied",
                "You don't have permission to perform this action",
                request,
                null
        );
        
        loggingService.logWarn("Access denied for request: {}", request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = buildErrorResponse(
                "INVALID_REQUEST",
                "Invalid request body",
                "The request body is invalid or malformed",
                request,
                null
        );
        
        loggingService.logWarn("Invalid request body: {}", ex.getMessage(),
                Map.of("path", request.getRequestURI()));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        String message = String.format("Parameter '%s' should be of type %s", 
                ex.getName(), ex.getRequiredType().getSimpleName());
        
        ErrorResponse errorResponse = buildErrorResponse(
                "INVALID_PARAMETER",
                message,
                "Invalid parameter format",
                request,
                null
        );
        
        loggingService.logWarn("Method argument type mismatch: {}", message,
                Map.of("path", request.getRequestURI(), "parameter", ex.getName()));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        String message = String.format("Required parameter '%s' is missing", ex.getParameterName());
        
        ErrorResponse errorResponse = buildErrorResponse(
                "MISSING_PARAMETER",
                message,
                "Required parameter is missing",
                request,
                null
        );
        
        loggingService.logWarn("Missing required parameter: {}", ex.getParameterName(),
                Map.of("path", request.getRequestURI()));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = buildErrorResponse(
                "NOT_FOUND",
                "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL(),
                "The requested endpoint was not found",
                request,
                null
        );
        
        loggingService.logWarn("No handler found: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = buildErrorResponse(
                "DATA_INTEGRITY_VIOLATION",
                "Data integrity violation",
                "The operation would violate data integrity constraints",
                request,
                null
        );
        
        loggingService.logError("Data integrity violation: {}", ex.getMessage(),
                Map.of("path", request.getRequestURI(), "method", request.getMethod()), ex);
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = buildErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                "Something went wrong. Please try again later.",
                request,
                ex.getMessage()
        );
        
        loggingService.logError("Unexpected error: {}", ex.getMessage(),
                Map.of("path", request.getRequestURI(), "method", request.getMethod()), ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    private ErrorResponse buildErrorResponse(String errorCode, String message, String userMessage,
                                           HttpServletRequest request, String details) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .errorCode(errorCode)
                .message(message)
                .userMessage(userMessage)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .details(details)
                .build();
    }
} 