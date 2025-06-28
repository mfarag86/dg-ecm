import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

export enum LogLevel {
  ERROR = 0,
  WARN = 1,
  INFO = 2,
  DEBUG = 3,
  TRACE = 4
}

@Injectable({
  providedIn: 'root'
})
export class LoggerService {
  private logLevel: LogLevel = environment.production ? LogLevel.ERROR : LogLevel.DEBUG;

  setLogLevel(level: LogLevel): void {
    this.logLevel = level;
  }

  error(message: string, ...args: any[]): void {
    if (this.logLevel >= LogLevel.ERROR) {
      console.error(`[ERROR] ${new Date().toISOString()}: ${message}`, ...args);
    }
  }

  warn(message: string, ...args: any[]): void {
    if (this.logLevel >= LogLevel.WARN) {
      console.warn(`[WARN] ${new Date().toISOString()}: ${message}`, ...args);
    }
  }

  info(message: string, ...args: any[]): void {
    if (this.logLevel >= LogLevel.INFO) {
      console.info(`[INFO] ${new Date().toISOString()}: ${message}`, ...args);
    }
  }

  debug(message: string, ...args: any[]): void {
    if (this.logLevel >= LogLevel.DEBUG) {
      console.debug(`[DEBUG] ${new Date().toISOString()}: ${message}`, ...args);
    }
  }

  trace(message: string, ...args: any[]): void {
    if (this.logLevel >= LogLevel.TRACE) {
      console.trace(`[TRACE] ${new Date().toISOString()}: ${message}`, ...args);
    }
  }

  logError(error: Error, context?: string): void {
    this.error(`${context || 'Application Error'}: ${error.message}`, {
      name: error.name,
      stack: error.stack,
      timestamp: new Date().toISOString()
    });
  }

  logHttpError(error: any, context?: string): void {
    this.error(`${context || 'HTTP Error'}: ${error.message || 'Unknown error'}`, {
      status: error.status,
      statusText: error.statusText,
      url: error.url,
      timestamp: new Date().toISOString()
    });
  }
} 