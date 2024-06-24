package com.example.file_management.controller;

import com.example.file_management.controller.api.ApiDataResponse;
import com.example.file_management.dto.LoginRequest;
import com.example.file_management.dto.LoginResponse;
import com.example.file_management.dto.UserDto;
import com.example.file_management.dto.UserRegisterRequest;
import com.example.file_management.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aldi
 * @since 18.06.2024
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiDataResponse<UserDto>> register(@Valid @RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok().body(ApiDataResponse.create(authService.registration(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse<LoginResponse>> register(@RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(ApiDataResponse.create(authService.login(request)));
    }

}
