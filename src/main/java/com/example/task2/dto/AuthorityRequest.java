package com.example.task2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityRequest {
    @NotBlank(message = "name cannot be blank")
    @Size(min = 3, max = 20, message = "name must be between 3 and 20 characters")
    private String name;
}
