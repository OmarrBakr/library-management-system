package com.example.task2.services;

import com.example.task2.config.JwtAuthenticationToken;
import com.example.task2.config.JwtService;
import com.example.task2.dto.ApiResponse;
import com.example.task2.dto.UpdateUserRequest;
import com.example.task2.entities.User;
import com.example.task2.exceptions.DuplicateUsernameException;
import com.example.task2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<ApiResponse<User>> updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new DuplicateUsernameException("Username is already taken");
            }
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);
        ApiResponse<User> response = ApiResponse.success("User updated successfully", user, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        ApiResponse<List<User>> response = ApiResponse.success("Users retrieved successfully", users, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<User>> getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        ApiResponse<User> response = ApiResponse.success("User retrieved successfully", user, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Void>> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
        ApiResponse<Void> response = ApiResponse.success("User deleted successfully", null, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
