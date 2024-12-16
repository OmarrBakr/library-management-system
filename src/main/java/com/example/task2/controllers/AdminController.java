package com.example.task2.controllers;

import com.example.task2.dto.ApiResponse;
import com.example.task2.entities.User;
import com.example.task2.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private final AuthService authService;

    @GetMapping
    public String demo(){
        return "admin controller";
    }

    @PatchMapping("toggle-enabled/{id}")
    public ResponseEntity<ApiResponse<User>> toggleUserEnabled(@PathVariable Long id) {
        return authService.toggleUserEnabled(id);
    }
}

