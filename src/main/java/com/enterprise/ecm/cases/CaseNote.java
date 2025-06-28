package com.enterprise.ecm.cases;

import com.enterprise.ecm.shared.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "case_notes")
public class CaseNote extends BaseEntity {
    
    @NotBlank
    @Column(name = "content")
    private String content;
    
    @Column(name = "note_type")
    private String noteType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Case caseEntity;
    
    public CaseNote() {}
    
    public CaseNote(String content) {
        this.content = content;
    }
    
    public CaseNote(String content, String noteType) {
        this.content = content;
        this.noteType = noteType;
    }
    
    // Getters and Setters
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getNoteType() {
        return noteType;
    }
    
    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }
    
    public Case getCaseEntity() {
        return caseEntity;
    }
    
    public void setCaseEntity(Case caseEntity) {
        this.caseEntity = caseEntity;
    }
} 