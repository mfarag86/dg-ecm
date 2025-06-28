package com.enterprise.ecm.shared.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Logs all incoming HTTP requests and outgoing responses, including bodies for JSON/text.
 */
@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private final LoggingService loggingService;
    private static final int MAX_BODY_LENGTH = 1000;

    public RequestResponseLoggingFilter(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String traceId = UUID.randomUUID().toString();
        request.setAttribute("traceId", traceId);
        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            loggingService.logInfo("Incoming request: {} {}?{} [traceId={}]", wrappedRequest.getMethod(), wrappedRequest.getRequestURI(), wrappedRequest.getQueryString(), traceId);
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String requestBody = getContentAsString(wrappedRequest.getContentAsByteArray(), wrappedRequest.getContentType());
            String responseBody = getContentAsString(wrappedResponse.getContentAsByteArray(), wrappedResponse.getContentType());
            loggingService.logInfo("Request body: {}", maskSensitive(requestBody));
            loggingService.logInfo("Response: {} {} [status={}, traceId={}, duration={}ms]", wrappedRequest.getMethod(), wrappedRequest.getRequestURI(), wrappedResponse.getStatus(), traceId, duration);
            loggingService.logInfo("Response body: {}", maskSensitive(responseBody));
            wrappedResponse.copyBodyToResponse();
        }
    }

    private String getContentAsString(byte[] buf, String contentType) {
        if (buf == null || buf.length == 0) return "";
        if (contentType == null) return "";
        String type = contentType.toLowerCase();
        if (type.contains("application/json") || type.contains("text") || type.contains("application/xml")) {
            String body = new String(buf, StandardCharsets.UTF_8);
            if (body.length() > MAX_BODY_LENGTH) {
                return body.substring(0, MAX_BODY_LENGTH) + "...[truncated]";
            }
            return body;
        }
        return "[non-text content]";
    }

    private String maskSensitive(String body) {
        if (body == null) return null;
        // Simple masking for common sensitive fields (e.g., password)
        return body.replaceAll("(\"password\"\\s*:\\s*)\".*?\"", "$1\"***MASKED***\"");
    }
} 