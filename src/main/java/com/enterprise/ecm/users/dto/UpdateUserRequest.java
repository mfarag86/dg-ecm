package com.enterprise.ecm.users.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private String position;
    private boolean active;
    private Set<String> roles;
} 