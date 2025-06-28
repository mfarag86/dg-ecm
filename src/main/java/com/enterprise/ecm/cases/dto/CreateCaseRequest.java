package com.enterprise.ecm.cases.dto;

import com.enterprise.ecm.cases.CasePriority;
import com.enterprise.ecm.cases.CaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateCaseRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Status is required")
    private CaseStatus status = CaseStatus.OPEN;
    
    @NotNull(message = "Priority is required")
    private CasePriority priority = CasePriority.MEDIUM;
    
    private String category;
    private String assignedTo;
    private LocalDateTime dueDate;
} 