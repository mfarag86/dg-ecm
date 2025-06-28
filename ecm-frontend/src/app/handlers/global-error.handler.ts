import { ErrorHandler, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { LoggerService } from '../services/logger.service';
import { ErrorHandlerService } from '../services/error-handler.service';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  constructor(
    private logger: LoggerService,
    private errorHandler: ErrorHandlerService
  ) {}

  handleError(error: Error): void {
    // Log the error with full details
    this.logger.error('Unhandled error caught by GlobalErrorHandler', {
      error: error,
      stack: error.stack,
      timestamp: new Date().toISOString(),
      userAgent: navigator.userAgent,
      url: window.location.href
    });

    // Handle the error through our error handler service
    this.errorHandler.handleError(error, 'GlobalErrorHandler');

    // In development, re-throw the error to see it in the console
    if (!environment.production) {
      console.error('Original error:', error);
    }
  }
} 