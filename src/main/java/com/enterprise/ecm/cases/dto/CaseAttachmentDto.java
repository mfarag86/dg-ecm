package com.enterprise.ecm.cases.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CaseAttachmentDto {
    private Long id;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
} 