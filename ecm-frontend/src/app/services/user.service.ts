import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError, tap } from 'rxjs';
import { User, CreateUserRequest, UpdateUserRequest } from '../models/user.model';
import { environment } from '../../environments/environment';
import { LoggerService } from './logger.service';
import { ErrorHandlerService } from './error-handler.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(
    private http: HttpClient,
    private logger: LoggerService,
    private errorHandler: ErrorHandlerService
  ) { }

  createUser(createUserRequest: CreateUserRequest): Observable<User> {
    this.logger.info('Creating new user', { 
      username: createUserRequest.username,
      email: createUserRequest.email,
      roles: createUserRequest.roles 
    });
    
    return this.http.post<User>(this.apiUrl, createUserRequest)
      .pipe(
        tap(user => {
          this.logger.info('User created successfully', { 
            userId: user.id,
            username: user.username 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to create user', { error, request: createUserRequest });
          this.errorHandler.handleHttpError(error, 'UserService.createUser');
          return throwError(() => error);
        })
      );
  }

  getAllUsers(page: number = 0, size: number = 10): Observable<any> {
    this.logger.debug('Fetching all users with pagination', { page, size });
    
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<any>(this.apiUrl, { params })
      .pipe(
        tap(response => {
          this.logger.debug('Users fetched successfully', { 
            totalElements: response.totalElements,
            totalPages: response.totalPages,
            currentPage: response.number 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch users', { page, size, error });
          this.errorHandler.handleHttpError(error, 'UserService.getAllUsers');
          return throwError(() => error);
        })
      );
  }

  getAllUsersList(): Observable<User[]> {
    this.logger.debug('Fetching all users list');
    
    return this.http.get<User[]>(`${this.apiUrl}/list`)
      .pipe(
        tap(users => {
          this.logger.debug('Users list fetched successfully', { count: users.length });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch users list', { error });
          this.errorHandler.handleHttpError(error, 'UserService.getAllUsersList');
          return throwError(() => error);
        })
      );
  }

  getUserById(id: number): Observable<User> {
    this.logger.debug('Fetching user by ID', { userId: id });
    
    return this.http.get<User>(`${this.apiUrl}/${id}`)
      .pipe(
        tap(user => {
          this.logger.debug('User fetched successfully by ID', { 
            userId: user.id,
            username: user.username 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch user by ID', { userId: id, error });
          this.errorHandler.handleHttpError(error, 'UserService.getUserById');
          return throwError(() => error);
        })
      );
  }

  getUserByUsername(username: string): Observable<User> {
    this.logger.debug('Fetching user by username', { username });
    
    return this.http.get<User>(`${this.apiUrl}/username/${username}`)
      .pipe(
        tap(user => {
          this.logger.debug('User fetched successfully by username', { 
            userId: user.id,
            username: user.username 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch user by username', { username, error });
          this.errorHandler.handleHttpError(error, 'UserService.getUserByUsername');
          return throwError(() => error);
        })
      );
  }

  getUserByEmail(email: string): Observable<User> {
    this.logger.debug('Fetching user by email', { email });
    
    return this.http.get<User>(`${this.apiUrl}/email/${email}`)
      .pipe(
        tap(user => {
          this.logger.debug('User fetched successfully by email', { 
            userId: user.id,
            email: user.email 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch user by email', { email, error });
          this.errorHandler.handleHttpError(error, 'UserService.getUserByEmail');
          return throwError(() => error);
        })
      );
  }

  getUsersByDepartment(department: string): Observable<User[]> {
    this.logger.debug('Fetching users by department', { department });
    
    return this.http.get<User[]>(`${this.apiUrl}/department/${department}`)
      .pipe(
        tap(users => {
          this.logger.debug('Users by department fetched successfully', { department, count: users.length });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch users by department', { department, error });
          this.errorHandler.handleHttpError(error, 'UserService.getUsersByDepartment');
          return throwError(() => error);
        })
      );
  }

  getUsersByRole(role: string): Observable<User[]> {
    this.logger.debug('Fetching users by role', { role });
    
    return this.http.get<User[]>(`${this.apiUrl}/role/${role}`)
      .pipe(
        tap(users => {
          this.logger.debug('Users by role fetched successfully', { role, count: users.length });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch users by role', { role, error });
          this.errorHandler.handleHttpError(error, 'UserService.getUsersByRole');
          return throwError(() => error);
        })
      );
  }

  getActiveUsers(): Observable<User[]> {
    this.logger.debug('Fetching active users');
    
    return this.http.get<User[]>(`${this.apiUrl}/active`)
      .pipe(
        tap(users => {
          this.logger.debug('Active users fetched successfully', { count: users.length });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch active users', { error });
          this.errorHandler.handleHttpError(error, 'UserService.getActiveUsers');
          return throwError(() => error);
        })
      );
  }

  updateUser(id: number, updateUserRequest: UpdateUserRequest): Observable<User> {
    this.logger.info('Updating user', { userId: id, updates: updateUserRequest });
    
    return this.http.put<User>(`${this.apiUrl}/${id}`, updateUserRequest)
      .pipe(
        tap(user => {
          this.logger.info('User updated successfully', { 
            userId: user.id,
            username: user.username 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to update user', { userId: id, error, updates: updateUserRequest });
          this.errorHandler.handleHttpError(error, 'UserService.updateUser');
          return throwError(() => error);
        })
      );
  }

  updatePassword(id: number, newPassword: string): Observable<User> {
    this.logger.info('Updating user password', { userId: id });
    
    const params = new HttpParams().set('newPassword', newPassword);
    
    return this.http.patch<User>(`${this.apiUrl}/${id}/password`, null, { params })
      .pipe(
        tap(user => {
          this.logger.info('User password updated successfully', { userId: user.id });
        }),
        catchError(error => {
          this.logger.error('Failed to update user password', { userId: id, error });
          this.errorHandler.handleHttpError(error, 'UserService.updatePassword');
          return throwError(() => error);
        })
      );
  }

  deactivateUser(id: number): Observable<void> {
    this.logger.warn('Deactivating user', { userId: id });
    
    return this.http.patch<void>(`${this.apiUrl}/${id}/deactivate`, null)
      .pipe(
        tap(() => {
          this.logger.info('User deactivated successfully', { userId: id });
        }),
        catchError(error => {
          this.logger.error('Failed to deactivate user', { userId: id, error });
          this.errorHandler.handleHttpError(error, 'UserService.deactivateUser');
          return throwError(() => error);
        })
      );
  }

  activateUser(id: number): Observable<void> {
    this.logger.info('Activating user', { userId: id });
    
    return this.http.patch<void>(`${this.apiUrl}/${id}/activate`, null)
      .pipe(
        tap(() => {
          this.logger.info('User activated successfully', { userId: id });
        }),
        catchError(error => {
          this.logger.error('Failed to activate user', { userId: id, error });
          this.errorHandler.handleHttpError(error, 'UserService.activateUser');
          return throwError(() => error);
        })
      );
  }

  deleteUser(id: number): Observable<void> {
    this.logger.warn('Deleting user', { userId: id });
    
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
        tap(() => {
          this.logger.info('User deleted successfully', { userId: id });
        }),
        catchError(error => {
          this.logger.error('Failed to delete user', { userId: id, error });
          this.errorHandler.handleHttpError(error, 'UserService.deleteUser');
          return throwError(() => error);
        })
      );
  }

  getUserCount(): Observable<number> {
    this.logger.debug('Fetching user count');
    
    return this.http.get<number>(`${this.apiUrl}/stats/count`)
      .pipe(
        tap(count => {
          this.logger.debug('User count fetched successfully', { count });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch user count', { error });
          this.errorHandler.handleHttpError(error, 'UserService.getUserCount');
          return throwError(() => error);
        })
      );
  }

  getActiveUserCount(): Observable<number> {
    this.logger.debug('Fetching active user count');
    
    return this.http.get<number>(`${this.apiUrl}/stats/active-count`)
      .pipe(
        tap(count => {
          this.logger.debug('Active user count fetched successfully', { count });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch active user count', { error });
          this.errorHandler.handleHttpError(error, 'UserService.getActiveUserCount');
          return throwError(() => error);
        })
      );
  }
} 