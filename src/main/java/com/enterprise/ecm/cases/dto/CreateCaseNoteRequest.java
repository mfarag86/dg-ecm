package com.enterprise.ecm.cases.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCaseNoteRequest {
    @NotBlank(message = "Content is required")
    private String content;
    
    private String noteType;
} 