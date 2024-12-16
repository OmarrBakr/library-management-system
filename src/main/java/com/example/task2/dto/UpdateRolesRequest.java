package com.example.task2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRolesRequest {

    @NotNull(message = "Role ID cannot be blank")
    private Long roleId;
}
