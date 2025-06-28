package com.enterprise.ecm.cases;

public enum CaseStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    PENDING("Pending"),
    RESOLVED("Resolved"),
    CLOSED("Closed"),
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    CaseStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
} 