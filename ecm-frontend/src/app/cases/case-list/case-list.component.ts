import { Component, OnInit } from '@angular/core';
import { CaseService } from '../../services/case.service';
import { Case } from '../../models/case.model';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-case-list',
  templateUrl: './case-list.component.html',
  styleUrls: ['./case-list.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ]
})
export class CaseListComponent implements OnInit {
  cases: Case[] = [];
  loading = true;

  constructor(private caseService: CaseService) {}

  ngOnInit(): void {
    this.loadCases();
  }

  private loadCases(): void {
    this.caseService.getAllCasesList().subscribe({
      next: (cases) => {
        this.cases = cases;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }
} 