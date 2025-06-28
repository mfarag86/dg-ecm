package com.enterprise.ecm.cases.dto;

import com.enterprise.ecm.cases.CasePriority;
import com.enterprise.ecm.cases.CaseStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CaseDto {
    private Long id;
    private String caseNumber;
    private String title;
    private String description;
    private CaseStatus status;
    private CasePriority priority;
    private String category;
    private String assignedTo;
    private LocalDateTime dueDate;
    private LocalDateTime resolvedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Long version;
    private List<CaseNoteDto> notes;
    private List<CaseAttachmentDto> attachments;
} 