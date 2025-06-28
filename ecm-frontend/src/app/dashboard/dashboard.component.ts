import { Component, OnInit } from '@angular/core';
import { CaseService } from '../services/case.service';
import { UserService } from '../services/user.service';
import { CaseStatus } from '../models/case.model';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatProgressBarModule,
    MatProgressSpinnerModule
  ]
})
export class DashboardComponent implements OnInit {
  totalCases = 0;
  openCases = 0;
  inProgressCases = 0;
  resolvedCases = 0;
  totalUsers = 0;
  activeUsers = 0;
  loading = true;

  constructor(
    private caseService: CaseService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  private loadDashboardData(): void {
    this.loading = true;
    
    // Load case statistics
    this.caseService.getCaseCount().subscribe(count => {
      this.totalCases = count;
    });

    this.caseService.getCaseCountByStatus(CaseStatus.OPEN).subscribe(count => {
      this.openCases = count;
    });

    this.caseService.getCaseCountByStatus(CaseStatus.IN_PROGRESS).subscribe(count => {
      this.inProgressCases = count;
    });

    this.caseService.getCaseCountByStatus(CaseStatus.RESOLVED).subscribe(count => {
      this.resolvedCases = count;
    });

    // Load user statistics
    this.userService.getUserCount().subscribe(count => {
      this.totalUsers = count;
    });

    this.userService.getActiveUserCount().subscribe(count => {
      this.activeUsers = count;
      this.loading = false;
    });
  }

  getCompletionRate(): number {
    if (this.totalCases === 0) return 0;
    return Math.round((this.resolvedCases / this.totalCases) * 100);
  }
} 