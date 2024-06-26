package com.example.file_management.service;

import com.example.file_management.dto.LoginRequest;
import com.example.file_management.dto.UserRegisterRequest;
import com.example.file_management.model.UserEntity;
import com.example.file_management.model.enums.UserRole;
import com.example.file_management.repository.UserRepository;
import com.example.file_management.utils.JwtTokenUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author aldi
 * @since 24.06.2024
 */

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private final String JWT = "mocked-jwt";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @InjectMocks
    private AuthService authService;

    @Test
    void registration_userAlreadyExists_throwsException() {
        var userRegisterRequest = createUserRegisterRequest();
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(BadCredentialsException.class, () -> authService.registration(userRegisterRequest));
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void registration_successfulRegistration_returnsUserDto() {
        var user = createUser();
        var userRegisterRequest = createUserRegisterRequest();
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        var result = authService.registration(userRegisterRequest);

        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals(user.getEmail(), result.email());
        assertEquals(user.getName(), result.name());
        assertEquals(user.getRole(), result.role());
        assertNotEquals(userRegisterRequest.password(), user.getPassword());

        verify(passwordEncoder).encode(eq(userRegisterRequest.password()));
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void login_successfulLogin_returnsLoginResponse() {
        var user = createUser();
        var loginRequest = createLoginRequest();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtTokenUtils.generateToken(user)).thenReturn(JWT);

        var result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals(loginRequest.email(), result.user().email());
        assertEquals(JWT, result.accessToken());
        assertEquals(user.getId(), result.user().id());
        assertEquals(user.getName(), result.user().name());
        assertEquals(user.getRole(), result.user().role());

        verify(authenticationManager).authenticate(argThat(authToken -> authToken.getPrincipal().equals(loginRequest.email())));
        verify(userRepository).findByEmail(eq(loginRequest.email()));
        verify(jwtTokenUtils).generateToken(user);
    }

    @Test
    void login_invalidCredentials_throwsBadCredentialsException() {
        var loginRequest = createLoginRequest();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        verify(userRepository, never()).findByEmail(anyString());
        verify(jwtTokenUtils, never()).generateToken(any());
    }

    @Test
    void login_userNotFound_throwsEntityNotFoundException() {
        var loginRequest = createLoginRequest();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(loginRequest.email())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> authService.login(loginRequest));
        verify(jwtTokenUtils, never()).generateToken(any());
    }

    private UserRegisterRequest createUserRegisterRequest() {
        return new UserRegisterRequest(
                "John",
                "test@example.com",
                "password"
        );
    }

    private UserEntity createUser() {
        var user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setName("John");
        user.setRole(UserRole.USER);
        return user;
    }

    private LoginRequest createLoginRequest() {
        return new LoginRequest("test@example.com", "password");
    }
}