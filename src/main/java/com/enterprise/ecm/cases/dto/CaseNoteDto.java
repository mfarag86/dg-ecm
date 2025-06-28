package com.enterprise.ecm.cases.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CaseNoteDto {
    private Long id;
    private String content;
    private String noteType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
} 