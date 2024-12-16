package com.example.task2.controllers;

import com.example.task2.dto.ApiResponse;
import com.example.task2.dto.AuthorityRequest;
import com.example.task2.dto.UpdateRolesRequest;
import com.example.task2.entities.Authority;
import com.example.task2.entities.User;
import com.example.task2.services.AuthorityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authorities")
@AllArgsConstructor
@Validated
public class AuthorityController {
    private final AuthorityService authorityService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Authority>>> getAllAuthorities() {
        return authorityService.getAllAuthorities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Authority>> getAuthority(@PathVariable Long id) {
        return authorityService.getAuthority(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Authority>> createAuthority(@Valid @RequestBody AuthorityRequest authorityRequest) {
        return authorityService.createAuthority(authorityRequest);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Authority>> updateAuthority(
            @PathVariable Long id,
            @Valid @RequestBody AuthorityRequest request) {
        return authorityService.updateAuthority(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAuthority(@PathVariable Long id) {
        return authorityService.deleteAuthority(id);
    }

    @PatchMapping("/user/{id}/role")
    public ResponseEntity<ApiResponse<User>> updateUserRoles(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRolesRequest request) {
        return authorityService.updateUserRoles(id, request);
    }
}
