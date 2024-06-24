package com.example.file_management.service;

import com.example.file_management.dto.LoginRequest;
import com.example.file_management.dto.LoginResponse;
import com.example.file_management.dto.UserDto;
import com.example.file_management.dto.UserRegisterRequest;
import com.example.file_management.mapper.UserMapper;
import com.example.file_management.model.enums.UserRole;
import com.example.file_management.repository.UserRepository;
import com.example.file_management.utils.JwtTokenUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author aldi
 * @since 17.06.2024
 */

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    public UserDto registration(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadCredentialsException("User with email %s already exists".formatted(request.email()));
        }

        var user = UserMapper.INSTANCE.mapToEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.USER);
        var savedUser = userRepository.save(user);

        return UserMapper.INSTANCE.mapToDto(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("User with email %s is not registered".formatted(request.email()));
        }

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        var token = jwtTokenUtils.generateToken(user);

        return new LoginResponse(UserMapper.INSTANCE.mapToDto(user), token);
    }
}
