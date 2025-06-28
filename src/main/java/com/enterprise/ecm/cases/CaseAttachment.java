package com.enterprise.ecm.cases;

import com.enterprise.ecm.shared.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "case_attachments")
public class CaseAttachment extends BaseEntity {
    
    @NotBlank
    @Column(name = "file_name")
    private String fileName;
    
    @NotBlank
    @Column(name = "file_path")
    private String filePath;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "content_type")
    private String contentType;
    
    @Column(name = "description")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Case caseEntity;
    
    public CaseAttachment() {}
    
    public CaseAttachment(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
    
    public CaseAttachment(String fileName, String filePath, Long fileSize, String contentType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.contentType = contentType;
    }
    
    // Getters and Setters
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Case getCaseEntity() {
        return caseEntity;
    }
    
    public void setCaseEntity(Case caseEntity) {
        this.caseEntity = caseEntity;
    }
} 