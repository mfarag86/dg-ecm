import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError, tap } from 'rxjs';
import { Case, CreateCaseRequest, UpdateCaseRequest, CaseStatus, CasePriority } from '../models/case.model';
import { environment } from '../../environments/environment';
import { LoggerService } from './logger.service';
import { ErrorHandlerService } from './error-handler.service';

@Injectable({
  providedIn: 'root'
})
export class CaseService {
  private apiUrl = `${environment.apiUrl}/cases`;

  constructor(
    private http: HttpClient,
    private logger: LoggerService,
    private errorHandler: ErrorHandlerService
  ) { }

  createCase(createCaseRequest: CreateCaseRequest): Observable<Case> {
    this.logger.info('Creating new case', { 
      title: createCaseRequest.title,
      priority: createCaseRequest.priority,
      status: createCaseRequest.status 
    });
    
    return this.http.post<Case>(this.apiUrl, createCaseRequest)
      .pipe(
        tap(caseData => {
          this.logger.info('Case created successfully', { 
            caseId: caseData.id,
            caseNumber: caseData.caseNumber 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to create case', { error, request: createCaseRequest });
          this.errorHandler.handleHttpError(error, 'CaseService.createCase');
          return throwError(() => error);
        })
      );
  }

  getCaseById(id: number): Observable<Case> {
    this.logger.debug('Fetching case by ID', { caseId: id });
    
    return this.http.get<Case>(`${this.apiUrl}/${id}`)
      .pipe(
        tap(caseData => {
          this.logger.debug('Case fetched successfully', { 
            caseId: caseData.id,
            caseNumber: caseData.caseNumber 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch case by ID', { caseId: id, error });
          this.errorHandler.handleHttpError(error, 'CaseService.getCaseById');
          return throwError(() => error);
        })
      );
  }

  getCaseByNumber(caseNumber: string): Observable<Case> {
    this.logger.debug('Fetching case by number', { caseNumber });
    
    return this.http.get<Case>(`${this.apiUrl}/number/${caseNumber}`)
      .pipe(
        tap(caseData => {
          this.logger.debug('Case fetched successfully by number', { 
            caseId: caseData.id,
            caseNumber: caseData.caseNumber 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch case by number', { caseNumber, error });
          this.errorHandler.handleHttpError(error, 'CaseService.getCaseByNumber');
          return throwError(() => error);
        })
      );
  }

  getAllCases(page: number = 0, size: number = 10): Observable<any> {
    this.logger.debug('Fetching all cases with pagination', { page, size });
    
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<any>(this.apiUrl, { params })
      .pipe(
        tap(response => {
          this.logger.debug('Cases fetched successfully', { 
            totalElements: response.totalElements,
            totalPages: response.totalPages,
            currentPage: response.number 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch cases', { page, size, error });
          this.errorHandler.handleHttpError(error, 'CaseService.getAllCases');
          return throwError(() => error);
        })
      );
  }

  getAllCasesList(): Observable<Case[]> {
    this.logger.debug('Fetching all cases list');
    
    return this.http.get<Case[]>(`${this.apiUrl}/list`)
      .pipe(
        tap(cases => {
          this.logger.debug('Cases list fetched successfully', { count: cases.length });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch cases list', { error });
          this.errorHandler.handleHttpError(error, 'CaseService.getAllCasesList');
          return throwError(() => error);
        })
      );
  }

  getCasesByStatus(status: CaseStatus): Observable<Case[]> {
    this.logger.debug('Fetching cases by status', { status });
    
    return this.http.get<Case[]>(`${this.apiUrl}/status/${status}`)
      .pipe(
        tap(cases => {
          this.logger.debug('Cases by status fetched successfully', { status, count: cases.length });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch cases by status', { status, error });
          this.errorHandler.handleHttpError(error, 'CaseService.getCasesByStatus');
          return throwError(() => error);
        })
      );
  }

  getCasesByPriority(priority: CasePriority): Observable<Case[]> {
    this.logger.debug('Fetching cases by priority', { priority });
    
    return this.http.get<Case[]>(`${this.apiUrl}/priority/${priority}`)
      .pipe(
        tap(cases => {
          this.logger.debug('Cases by priority fetched successfully', { priority, count: cases.length });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch cases by priority', { priority, error });
          this.errorHandler.handleHttpError(error, 'CaseService.getCasesByPriority');
          return throwError(() => error);
        })
      );
  }

  getCasesByAssignee(assignedTo: string): Observable<Case[]> {
    this.logger.debug('Fetching cases by assignee', { assignedTo });
    
    return this.http.get<Case[]>(`${this.apiUrl}/assignee/${assignedTo}`)
      .pipe(
        tap(cases => {
          this.logger.debug('Cases by assignee fetched successfully', { assignedTo, count: cases.length });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch cases by assignee', { assignedTo, error });
          this.errorHandler.handleHttpError(error, 'CaseService.getCasesByAssignee');
          return throwError(() => error);
        })
      );
  }

  getOverdueCases(): Observable<Case[]> {
    this.logger.debug('Fetching overdue cases');
    
    return this.http.get<Case[]>(`${this.apiUrl}/overdue`)
      .pipe(
        tap(cases => {
          this.logger.debug('Overdue cases fetched successfully', { count: cases.length });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch overdue cases', { error });
          this.errorHandler.handleHttpError(error, 'CaseService.getOverdueCases');
          return throwError(() => error);
        })
      );
  }

  searchCases(keyword: string): Observable<Case[]> {
    this.logger.debug('Searching cases', { keyword });
    
    const params = new HttpParams().set('keyword', keyword);
    
    return this.http.get<Case[]>(`${this.apiUrl}/search`, { params })
      .pipe(
        tap(cases => {
          this.logger.debug('Case search completed successfully', { keyword, count: cases.length });
        }),
        catchError(error => {
          this.logger.error('Failed to search cases', { keyword, error });
          this.errorHandler.handleHttpError(error, 'CaseService.searchCases');
          return throwError(() => error);
        })
      );
  }

  updateCase(id: number, updateCaseRequest: UpdateCaseRequest): Observable<Case> {
    this.logger.info('Updating case', { caseId: id, updates: updateCaseRequest });
    
    return this.http.put<Case>(`${this.apiUrl}/${id}`, updateCaseRequest)
      .pipe(
        tap(caseData => {
          this.logger.info('Case updated successfully', { 
            caseId: caseData.id,
            caseNumber: caseData.caseNumber 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to update case', { caseId: id, error, updates: updateCaseRequest });
          this.errorHandler.handleHttpError(error, 'CaseService.updateCase');
          return throwError(() => error);
        })
      );
  }

  updateCaseStatus(id: number, status: CaseStatus): Observable<Case> {
    this.logger.info('Updating case status', { caseId: id, newStatus: status });
    
    const params = new HttpParams().set('status', status);
    
    return this.http.patch<Case>(`${this.apiUrl}/${id}/status`, null, { params })
      .pipe(
        tap(caseData => {
          this.logger.info('Case status updated successfully', { 
            caseId: caseData.id,
            newStatus: caseData.status 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to update case status', { caseId: id, newStatus: status, error });
          this.errorHandler.handleHttpError(error, 'CaseService.updateCaseStatus');
          return throwError(() => error);
        })
      );
  }

  assignCase(id: number, assignedTo: string): Observable<Case> {
    this.logger.info('Assigning case', { caseId: id, assignedTo });
    
    const params = new HttpParams().set('assignedTo', assignedTo);
    
    return this.http.patch<Case>(`${this.apiUrl}/${id}/assign`, null, { params })
      .pipe(
        tap(caseData => {
          this.logger.info('Case assigned successfully', { 
            caseId: caseData.id,
            assignedTo: caseData.assignedTo 
          });
        }),
        catchError(error => {
          this.logger.error('Failed to assign case', { caseId: id, assignedTo, error });
          this.errorHandler.handleHttpError(error, 'CaseService.assignCase');
          return throwError(() => error);
        })
      );
  }

  deleteCase(id: number): Observable<void> {
    this.logger.warn('Deleting case', { caseId: id });
    
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
        tap(() => {
          this.logger.info('Case deleted successfully', { caseId: id });
        }),
        catchError(error => {
          this.logger.error('Failed to delete case', { caseId: id, error });
          this.errorHandler.handleHttpError(error, 'CaseService.deleteCase');
          return throwError(() => error);
        })
      );
  }

  getCaseCount(): Observable<number> {
    this.logger.debug('Fetching case count');
    
    return this.http.get<number>(`${this.apiUrl}/stats/count`)
      .pipe(
        tap(count => {
          this.logger.debug('Case count fetched successfully', { count });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch case count', { error });
          this.errorHandler.handleHttpError(error, 'CaseService.getCaseCount');
          return throwError(() => error);
        })
      );
  }

  getCaseCountByStatus(status: CaseStatus): Observable<number> {
    this.logger.debug('Fetching case count by status', { status });
    
    return this.http.get<number>(`${this.apiUrl}/stats/count/${status}`)
      .pipe(
        tap(count => {
          this.logger.debug('Case count by status fetched successfully', { status, count });
        }),
        catchError(error => {
          this.logger.error('Failed to fetch case count by status', { status, error });
          this.errorHandler.handleHttpError(error, 'CaseService.getCaseCountByStatus');
          return throwError(() => error);
        })
      );
  }
} 