package com.example.task2.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}

