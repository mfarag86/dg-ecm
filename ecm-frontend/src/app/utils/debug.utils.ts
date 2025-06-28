import { environment } from '../../environments/environment';

/**
 * Debug utility functions for development
 */
export class DebugUtils {
  /**
   * Log debug information only in development mode
   */
  static debug(message: string, data?: any): void {
    if (!environment.production) {
      console.debug(`[DEBUG] ${message}`, data);
    }
  }

  /**
   * Log performance metrics
   */
  static logPerformance(label: string, startTime: number): void {
    if (!environment.production) {
      const endTime = performance.now();
      const duration = endTime - startTime;
      console.debug(`[PERFORMANCE] ${label}: ${duration.toFixed(2)}ms`);
    }
  }

  /**
   * Create a performance timer
   */
  static startTimer(label: string): () => void {
    const startTime = performance.now();
    return () => this.logPerformance(label, startTime);
  }

  /**
   * Log component lifecycle events
   */
  static logLifecycle(componentName: string, event: string, data?: any): void {
    if (!environment.production) {
      console.debug(`[LIFECYCLE] ${componentName}.${event}`, data);
    }
  }

  /**
   * Log HTTP request/response details
   */
  static logHttpRequest(method: string, url: string, data?: any): void {
    if (!environment.production) {
      console.debug(`[HTTP REQUEST] ${method} ${url}`, data);
    }
  }

  static logHttpResponse(method: string, url: string, status: number, data?: any): void {
    if (!environment.production) {
      console.debug(`[HTTP RESPONSE] ${method} ${url} (${status})`, data);
    }
  }

  /**
   * Log form validation errors
   */
  static logFormErrors(formName: string, errors: any): void {
    if (!environment.production) {
      console.debug(`[FORM ERRORS] ${formName}:`, errors);
    }
  }

  /**
   * Log route changes
   */
  static logRouteChange(from: string, to: string): void {
    if (!environment.production) {
      console.debug(`[ROUTE] ${from} → ${to}`);
    }
  }

  /**
   * Log authentication events
   */
  static logAuthEvent(event: string, data?: any): void {
    if (!environment.production) {
      console.debug(`[AUTH] ${event}`, data);
    }
  }

  /**
   * Log user actions
   */
  static logUserAction(action: string, data?: any): void {
    if (!environment.production) {
      console.debug(`[USER ACTION] ${action}`, data);
    }
  }

  /**
   * Log error with stack trace
   */
  static logError(error: Error, context?: string): void {
    if (!environment.production) {
      console.error(`[ERROR] ${context || 'Application Error'}:`, {
        message: error.message,
        stack: error.stack,
        timestamp: new Date().toISOString()
      });
    }
  }

  /**
   * Log memory usage (if available)
   */
  static logMemoryUsage(): void {
    if (!environment.production && 'memory' in performance) {
      const memory = (performance as any).memory;
      console.debug('[MEMORY]', {
        used: `${Math.round(memory.usedJSHeapSize / 1024 / 1024)}MB`,
        total: `${Math.round(memory.totalJSHeapSize / 1024 / 1024)}MB`,
        limit: `${Math.round(memory.jsHeapSizeLimit / 1024 / 1024)}MB`
      });
    }
  }

  /**
   * Get current timestamp
   */
  static getTimestamp(): string {
    return new Date().toISOString();
  }

  /**
   * Generate a unique ID for tracking
   */
  static generateId(): string {
    return Math.random().toString(36).substr(2, 9);
  }
} 