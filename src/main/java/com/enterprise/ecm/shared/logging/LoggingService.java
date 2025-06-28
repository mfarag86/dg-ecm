package com.enterprise.ecm.shared.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Centralized logging service for the ECM backend.
 */
@Slf4j
@Service
public class LoggingService {
    public void logInfo(String message, Object... args) {
        log.info(message, args);
    }
    public void logInfo(String message, Map<String, Object> context) {
        log.info("{} | context={}", message, context);
    }
    public void logWarn(String message, Object... args) {
        log.warn(message, args);
    }
    public void logWarn(String message, Map<String, Object> context) {
        log.warn("{} | context={}", message, context);
    }
    public void logError(String message, Object... args) {
        log.error(message, args);
    }
    public void logError(String message, Map<String, Object> context, Throwable t) {
        log.error("{} | context={}", message, context, t);
    }
    public void logDebug(String message, Object... args) {
        log.debug(message, args);
    }
    public void logTrace(String message, Object... args) {
        log.trace(message, args);
    }
} 