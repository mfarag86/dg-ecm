export interface Case {
  id?: number;
  caseNumber: string;
  title: string;
  description: string;
  status: CaseStatus;
  priority: CasePriority;
  category: string;
  assignedTo: string;
  dueDate?: Date;
  resolvedDate?: Date;
  createdAt?: Date;
  updatedAt?: Date;
  createdBy?: string;
  updatedBy?: string;
  version?: number;
  notes?: CaseNote[];
  attachments?: CaseAttachment[];
}

export enum CaseStatus {
  OPEN = 'OPEN',
  IN_PROGRESS = 'IN_PROGRESS',
  PENDING = 'PENDING',
  RESOLVED = 'RESOLVED',
  CLOSED = 'CLOSED'
}

export enum CasePriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL'
}

export interface CaseNote {
  id?: number;
  content: string;
  createdBy?: string;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface CaseAttachment {
  id?: number;
  fileName: string;
  fileType: string;
  fileSize: number;
  uploadDate?: Date;
  uploadedBy?: string;
  filePath?: string;
}

export interface CreateCaseRequest {
  caseNumber: string;
  title: string;
  description: string;
  status: CaseStatus;
  priority: CasePriority;
  category: string;
  assignedTo: string;
  dueDate?: Date;
}

export interface UpdateCaseRequest {
  title?: string;
  description?: string;
  status?: CaseStatus;
  priority?: CasePriority;
  category?: string;
  assignedTo?: string;
  dueDate?: Date;
} 