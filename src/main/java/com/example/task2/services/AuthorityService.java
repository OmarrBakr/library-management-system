package com.example.task2.services;

import com.example.task2.dto.ApiResponse;
import com.example.task2.dto.AuthorityRequest;
import com.example.task2.dto.UpdateRolesRequest;
import com.example.task2.entities.Authority;
import com.example.task2.entities.User;
import com.example.task2.repositories.AuthorityRepository;
import com.example.task2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    public ResponseEntity<ApiResponse<List<Authority>>> getAllAuthorities() {
        List<Authority> authorities = authorityRepository.findAll();
        ApiResponse<List<Authority>> response = ApiResponse.success("Authorities retrieved successfully", authorities, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Authority>> getAuthority(Long id) {
        Authority authority = authorityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Authority not found with id: " + id));
        ApiResponse<Authority> response = ApiResponse.success("Authority retrieved successfully", authority, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Authority>> createAuthority(AuthorityRequest authority) {
        Authority savedAuthority = new Authority();
        savedAuthority.setName(authority.getName());
        authorityRepository.save(savedAuthority);
        ApiResponse<Authority> response = ApiResponse.success("Authority created successfully", savedAuthority, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ApiResponse<Authority>> updateAuthority(Long id, AuthorityRequest request) {
        Authority authority = authorityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Authority not found with id: " + id));
        authority.setName(request.getName());
        authorityRepository.save(authority);
        ApiResponse<Authority> response = ApiResponse.success("Authority updated successfully", authority, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Void>> deleteAuthority(Long id) {
        authorityRepository.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success("Authority deleted successfully", null, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<User>> updateUserRoles(Long userId, UpdateRolesRequest request) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
            Authority authority = authorityRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + request.getRoleId()));
            user.setAuthority(authority);
            userRepository.save(user);
            ApiResponse<User> response = ApiResponse.success("User role updated successfully", user, HttpStatus.OK);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("An error occurred while updating user role", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }


}
