# Logging and Error Handling Documentation

## Overview

This document describes the comprehensive logging and error handling system implemented in the ECM frontend application.

## Architecture

### 1. Logger Service (`LoggerService`)

The `LoggerService` provides centralized logging functionality with different log levels:

- **ERROR** (0): Critical errors that need immediate attention
- **WARN** (1): Warning messages for potential issues
- **INFO** (2): General information about application flow
- **DEBUG** (3): Detailed debugging information
- **TRACE** (4): Very detailed tracing information

#### Features:
- Environment-based log level configuration
- Timestamped log entries
- Structured logging with metadata
- HTTP error logging with status codes and URLs
- Error logging with stack traces

#### Usage:
```typescript
constructor(private logger: LoggerService) {}

// Basic logging
this.logger.info('User logged in', { userId: 123, username: 'john' });
this.logger.error('Failed to fetch data', { error, context: 'UserService' });

// HTTP error logging
this.logger.logHttpError(error, 'AuthService.login');

// Error logging with context
this.logger.logError(error, 'Component initialization');
```

### 2. Error Handler Service (`ErrorHandlerService`)

The `ErrorHandlerService` provides centralized error handling with user-friendly messages:

#### Features:
- HTTP status code mapping to user-friendly messages
- Different severity levels (error, warning, info)
- Automatic error logging
- User notification via snackbar
- Configurable error display

#### HTTP Status Code Handling:
- **400**: Invalid request. Please check your input.
- **401**: You are not authorized to perform this action.
- **403**: Access denied. You don't have permission for this action.
- **404**: The requested resource was not found.
- **409**: This resource already exists.
- **422**: Validation error. Please check your input.
- **500**: Server error. Please try again later.
- **502/503/504**: Service temporarily unavailable. Please try again later.
- **0**: Network error. Please check your connection.

#### Usage:
```typescript
constructor(private errorHandler: ErrorHandlerService) {}

// Handle HTTP errors
this.errorHandler.handleHttpError(error, 'UserService.createUser');

// Handle general errors
this.errorHandler.handleError(error, 'Component initialization');

// Show success/info/warning messages
this.errorHandler.showSuccess('User created successfully');
this.errorHandler.showInfo('Please wait while we process your request');
this.errorHandler.showWarning('This action cannot be undone');
```

### 3. Global Error Handler (`GlobalErrorHandler`)

The `GlobalErrorHandler` catches unhandled errors throughout the application:

#### Features:
- Catches all unhandled errors
- Logs errors with full context (stack trace, user agent, URL)
- Provides user-friendly error messages
- Development mode error re-throwing for debugging

### 4. Debug Utilities (`DebugUtils`)

Development-only utility functions for debugging:

#### Features:
- Performance timing
- Component lifecycle logging
- HTTP request/response logging
- Form validation error logging
- Route change logging
- Memory usage monitoring
- User action tracking

#### Usage:
```typescript
import { DebugUtils } from '../utils/debug.utils';

// Performance timing
const stopTimer = DebugUtils.startTimer('Data loading');
// ... perform operation
stopTimer();

// Lifecycle logging
DebugUtils.logLifecycle('UserComponent', 'ngOnInit', { userId: 123 });

// HTTP logging
DebugUtils.logHttpRequest('POST', '/api/users', userData);
DebugUtils.logHttpResponse('POST', '/api/users', 201, response);

// Route logging
DebugUtils.logRouteChange('/login', '/dashboard');
```

## Configuration

### Environment-based Configuration

Log levels are automatically configured based on the environment:

- **Development**: DEBUG level (shows all logs)
- **Production**: ERROR level (shows only errors)

### Customizing Log Levels

You can customize log levels in your components:

```typescript
constructor(private logger: LoggerService) {
  // Set custom log level for this component
  this.logger.setLogLevel(LogLevel.INFO);
}
```

## Integration with Services

### Auth Service
- Logs login attempts, successes, and failures
- Logs user registration events
- Logs authentication status checks
- Handles HTTP errors with user-friendly messages

### Case Service
- Logs CRUD operations with case details
- Logs search and filter operations
- Logs case assignment and status changes
- Comprehensive error handling for all operations

### User Service
- Logs user management operations
- Logs user activation/deactivation
- Logs password changes
- Error handling for all user operations

## Best Practices

### 1. Logging Best Practices
- Use appropriate log levels
- Include relevant context data
- Avoid logging sensitive information (passwords, tokens)
- Use structured logging with objects
- Log both success and failure scenarios

### 2. Error Handling Best Practices
- Always handle HTTP errors in services
- Provide user-friendly error messages
- Log errors with sufficient context
- Don't expose internal error details to users
- Use appropriate error severity levels

### 3. Performance Considerations
- Debug logs are automatically disabled in production
- Use performance timers for expensive operations
- Monitor memory usage in development
- Avoid excessive logging in production

## Monitoring and Debugging

### Development Tools
- Browser console for real-time log viewing
- Network tab for HTTP request monitoring
- Performance tab for timing analysis
- Memory tab for memory usage monitoring

### Production Monitoring
- Error logs are preserved for analysis
- HTTP errors include status codes and URLs
- User actions are tracked for debugging
- Performance metrics are available

## Customization

### Adding Custom Error Types
```typescript
// In ErrorHandlerService
private parseCustomError(error: any): ErrorInfo {
  // Add custom error parsing logic
  return {
    message: error.message,
    userMessage: 'Custom error message',
    severity: 'error',
    showToUser: true
  };
}
```

### Adding Custom Log Categories
```typescript
// In LoggerService
logCustomEvent(category: string, event: string, data?: any): void {
  this.info(`[${category}] ${event}`, data);
}
```

## Troubleshooting

### Common Issues
1. **Logs not appearing**: Check log level configuration
2. **Error messages not showing**: Verify ErrorHandlerService injection
3. **Performance issues**: Check for excessive debug logging
4. **Memory leaks**: Monitor memory usage with DebugUtils

### Debug Mode
Enable debug mode by setting the environment to development or manually setting log levels to DEBUG.

## Future Enhancements

- Remote logging to external services
- Log aggregation and analysis
- Real-time error monitoring
- Performance analytics dashboard
- Custom log formatters
- Log retention policies 