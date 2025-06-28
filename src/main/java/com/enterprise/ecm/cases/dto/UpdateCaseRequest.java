package com.enterprise.ecm.cases.dto;

import com.enterprise.ecm.cases.CasePriority;
import com.enterprise.ecm.cases.CaseStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateCaseRequest {
    private String title;
    private String description;
    private CaseStatus status;
    private CasePriority priority;
    private String category;
    private String assignedTo;
    private LocalDateTime dueDate;
} 