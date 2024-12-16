package com.example.task2.services;

import com.example.task2.config.JwtService;
import com.example.task2.dto.*;
import com.example.task2.entities.Authority;
import com.example.task2.entities.User;
import com.example.task2.exceptions.DuplicateUsernameException;
import com.example.task2.exceptions.UnauthenticatedException;
import com.example.task2.repositories.AuthorityRepository;
import com.example.task2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final AuthorityRepository authorityRepository;
  private final UserRepository userRepository;

  public ResponseEntity<ApiResponse<RegisterResponse>> register(RegisterRequest request) {
    if (repository.findByUsername(request.getUsername()).isPresent()) {
      throw new DuplicateUsernameException("Username is already taken");
    }
    Long authorityId = repository.count() == 0 ? 1L : 2L;
    Authority authority = authorityRepository.findById(authorityId)
            .orElseThrow(() -> new RuntimeException("Authority not found"));
    var user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .enabled(true)
            .authority(authority)
            .build();
    repository.save(user);
    var jwtToken = jwtService.generateToken(user, user.getId());
    RegisterResponse registerResponse = RegisterResponse.builder()
            .user(user)
            .token(jwtToken)
            .build();
    ApiResponse<RegisterResponse> apiResponse = ApiResponse.success("User registered successfully", registerResponse, HttpStatus.CREATED);
    return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
  }

  public ResponseEntity<ApiResponse<LoginResponse>> authenticate(LoginRequest request) {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
      );
      User user = repository.findByUsername(request.getUsername())
              .orElseThrow(() -> new UnauthenticatedException("Invalid username or password"));
      String jwtToken = jwtService.generateToken(user, user.getId());
      LoginResponse loginResponse = LoginResponse.builder()
              .user(user)
              .token(jwtToken)
              .build();
      ApiResponse<LoginResponse> apiResponse = ApiResponse.success("User authenticated successfully", loginResponse, HttpStatus.OK);
      return ResponseEntity.ok(apiResponse);
    } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
      throw new UnauthenticatedException("Invalid username or password");
    }
  }

  public ResponseEntity<ApiResponse<User>> toggleUserEnabled(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    user.setEnabled(!user.isEnabled());
    userRepository.save(user);
    ApiResponse<User> response = ApiResponse.success("User enabled status updated successfully", user, HttpStatus.OK);
    return ResponseEntity.ok(response);
  }
}
