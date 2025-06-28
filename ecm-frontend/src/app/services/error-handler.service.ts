import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoggerService } from './logger.service';

export interface ErrorInfo {
  message: string;
  userMessage?: string;
  context?: string;
  severity?: 'error' | 'warning' | 'info';
  showToUser?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {
  constructor(
    private logger: LoggerService,
    private snackBar: MatSnackBar
  ) {}

  handleError(error: any, context?: string): void {
    const errorInfo = this.parseError(error, context);
    
    // Log the error
    this.logger.logError(error, context);
    
    // Show user-friendly message if needed
    if (errorInfo.showToUser) {
      this.showUserMessage(errorInfo.userMessage || errorInfo.message, errorInfo.severity);
    }
  }

  handleHttpError(error: any, context?: string): void {
    const errorInfo = this.parseHttpError(error, context);
    
    // Log the HTTP error
    this.logger.logHttpError(error, context);
    
    // Show user-friendly message if needed
    if (errorInfo.showToUser) {
      this.showUserMessage(errorInfo.userMessage || errorInfo.message, errorInfo.severity);
    }
  }

  private parseError(error: any, context?: string): ErrorInfo {
    if (error instanceof Error) {
      return {
        message: error.message,
        userMessage: this.getUserFriendlyMessage(error.message),
        context,
        severity: 'error',
        showToUser: true
      };
    }
    
    return {
      message: error?.message || 'An unknown error occurred',
      userMessage: 'Something went wrong. Please try again.',
      context,
      severity: 'error',
      showToUser: true
    };
  }

  private parseHttpError(error: any, context?: string): ErrorInfo {
    const status = error?.status;
    const message = error?.message || 'Network error';
    
    let userMessage = 'Something went wrong. Please try again.';
    let severity: 'error' | 'warning' | 'info' = 'error';
    let showToUser = true;

    switch (status) {
      case 400:
        userMessage = 'Invalid request. Please check your input.';
        break;
      case 401:
        userMessage = 'You are not authorized to perform this action.';
        break;
      case 403:
        userMessage = 'Access denied. You don\'t have permission for this action.';
        break;
      case 404:
        userMessage = 'The requested resource was not found.';
        break;
      case 409:
        userMessage = 'This resource already exists.';
        severity = 'warning';
        break;
      case 422:
        userMessage = 'Validation error. Please check your input.';
        break;
      case 500:
        userMessage = 'Server error. Please try again later.';
        break;
      case 502:
      case 503:
      case 504:
        userMessage = 'Service temporarily unavailable. Please try again later.';
        break;
      case 0:
        userMessage = 'Network error. Please check your connection.';
        break;
      default:
        if (status >= 400 && status < 500) {
          userMessage = 'Client error. Please check your request.';
        } else if (status >= 500) {
          userMessage = 'Server error. Please try again later.';
        }
    }

    return {
      message,
      userMessage,
      context,
      severity,
      showToUser
    };
  }

  private getUserFriendlyMessage(errorMessage: string): string {
    // Map common error messages to user-friendly versions
    const errorMap: { [key: string]: string } = {
      'Network Error': 'Network error. Please check your connection.',
      'Timeout': 'Request timed out. Please try again.',
      'Unauthorized': 'You are not authorized to perform this action.',
      'Forbidden': 'Access denied. You don\'t have permission for this action.',
      'Not Found': 'The requested resource was not found.',
      'Internal Server Error': 'Server error. Please try again later.',
      'Bad Request': 'Invalid request. Please check your input.',
      'Validation Error': 'Please check your input and try again.'
    };

    return errorMap[errorMessage] || 'Something went wrong. Please try again.';
  }

  private showUserMessage(message: string, severity: 'error' | 'warning' | 'info' = 'error'): void {
    const duration = severity === 'error' ? 5000 : 3000;
    
    this.snackBar.open(message, 'Close', {
      duration,
      panelClass: [`snackbar-${severity}`]
    });
  }

  showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['snackbar-success']
    });
  }

  showInfo(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['snackbar-info']
    });
  }

  showWarning(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 4000,
      panelClass: ['snackbar-warning']
    });
  }
} 