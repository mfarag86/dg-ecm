import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, throwError } from 'rxjs';
import { LoginRequest, LoginResponse, RegisterRequest } from '../models/auth.model';
import { User } from '../models/user.model';
import { environment } from '../../environments/environment';
import { LoggerService } from './logger.service';
import { ErrorHandlerService } from './error-handler.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private logger: LoggerService,
    private errorHandler: ErrorHandlerService
  ) {
    this.loadCurrentUser();
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    this.logger.info('Attempting login', { username: loginRequest.username });
    
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, loginRequest)
      .pipe(
        tap(response => {
          this.logger.info('Login successful', { username: loginRequest.username });
          this.setToken(response.token);
          this.setUsername(response.username);
          this.loadCurrentUser();
        }),
        catchError(error => {
          this.logger.error('Login failed', { username: loginRequest.username, error });
          this.errorHandler.handleHttpError(error, 'AuthService.login');
          return throwError(() => error);
        })
      );
  }

  register(registerRequest: RegisterRequest): Observable<User> {
    this.logger.info('Attempting user registration', { username: registerRequest.username });
    
    return this.http.post<User>(`${this.apiUrl}/register`, registerRequest)
      .pipe(
        tap(user => {
          this.logger.info('User registration successful', { username: registerRequest.username });
        }),
        catchError(error => {
          this.logger.error('User registration failed', { username: registerRequest.username, error });
          this.errorHandler.handleHttpError(error, 'AuthService.register');
          return throwError(() => error);
        })
      );
  }

  logout(): void {
    this.logger.info('User logout', { username: this.getUsername() });
    this.removeToken();
    this.removeUsername();
    this.currentUserSubject.next(null);
  }

  getCurrentUser(): Observable<User> {
    this.logger.debug('Fetching current user');
    
    return this.http.get<User>(`${this.apiUrl}/me`)
      .pipe(
        tap(user => {
          this.logger.debug('Current user fetched successfully', { userId: user.id, username: user.username });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch current user', { error });
          this.errorHandler.handleHttpError(error, 'AuthService.getCurrentUser');
          return throwError(() => error);
        })
      );
  }

  private loadCurrentUser(): void {
    const token = this.getToken();
    if (token) {
      this.logger.debug('Loading current user from token');
      this.getCurrentUser().subscribe({
        next: (user) => {
          this.currentUserSubject.next(user);
          this.logger.info('Current user loaded successfully', { userId: user.id, username: user.username });
        },
        error: (error) => {
          this.logger.warn('Failed to load current user, logging out', { error });
          this.logout();
        }
      });
    } else {
      this.logger.debug('No token found, user not authenticated');
    }
  }

  isAuthenticated(): boolean {
    const authenticated = !!this.getToken();
    this.logger.debug('Checking authentication status', { authenticated });
    return authenticated;
  }

  getToken(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('token');
    }
    return null;
  }

  getUsername(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('username');
    }
    return null;
  }

  private setToken(token: string): void {
    if (typeof window !== 'undefined') {
      localStorage.setItem('token', token);
      this.logger.debug('Token stored in localStorage');
    }
  }

  private setUsername(username: string): void {
    if (typeof window !== 'undefined') {
      localStorage.setItem('username', username);
      this.logger.debug('Username stored in localStorage');
    }
  }

  private removeToken(): void {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('token');
      this.logger.debug('Token removed from localStorage');
    }
  }

  private removeUsername(): void {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('username');
      this.logger.debug('Username removed from localStorage');
    }
  }
} 