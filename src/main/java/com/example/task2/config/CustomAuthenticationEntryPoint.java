package com.example.task2.config;

import com.example.task2.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ApiResponse<String> apiResponse = ApiResponse.failure("Unauthenticated", HttpStatus.UNAUTHORIZED);

        String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);

        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}
