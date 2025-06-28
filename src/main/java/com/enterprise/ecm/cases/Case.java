package com.enterprise.ecm.cases;

import com.enterprise.ecm.shared.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cases")
public class Case extends BaseEntity {
    
    @NotBlank
    @Column(name = "case_number", unique = true)
    private String caseNumber;
    
    @NotBlank
    @Column(name = "title")
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CaseStatus status;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private CasePriority priority;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "assigned_to")
    private String assignedTo;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @Column(name = "resolved_date")
    private LocalDateTime resolvedDate;
    
    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CaseNote> notes = new ArrayList<>();
    
    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CaseAttachment> attachments = new ArrayList<>();
    
    public Case() {
        this.status = CaseStatus.OPEN;
        this.priority = CasePriority.MEDIUM;
    }
    
    public Case(String caseNumber, String title, String description) {
        this();
        this.caseNumber = caseNumber;
        this.title = title;
        this.description = description;
    }
    
    // Getters and Setters
    public String getCaseNumber() {
        return caseNumber;
    }
    
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public CaseStatus getStatus() {
        return status;
    }
    
    public void setStatus(CaseStatus status) {
        this.status = status;
        if (status == CaseStatus.RESOLVED && resolvedDate == null) {
            this.resolvedDate = LocalDateTime.now();
        }
    }
    
    public CasePriority getPriority() {
        return priority;
    }
    
    public void setPriority(CasePriority priority) {
        this.priority = priority;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDateTime getResolvedDate() {
        return resolvedDate;
    }
    
    public void setResolvedDate(LocalDateTime resolvedDate) {
        this.resolvedDate = resolvedDate;
    }
    
    public List<CaseNote> getNotes() {
        return notes;
    }
    
    public void setNotes(List<CaseNote> notes) {
        this.notes = notes;
    }
    
    public List<CaseAttachment> getAttachments() {
        return attachments;
    }
    
    public void setAttachments(List<CaseAttachment> attachments) {
        this.attachments = attachments;
    }
    
    public void addNote(CaseNote note) {
        note.setCaseEntity(this);
        this.notes.add(note);
    }
    
    public void addAttachment(CaseAttachment attachment) {
        attachment.setCaseEntity(this);
        this.attachments.add(attachment);
    }
} 