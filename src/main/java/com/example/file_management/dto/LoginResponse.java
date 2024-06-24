package com.example.file_management.dto;

import lombok.Getter;

/**
 * @author aldi
 * @since 18.06.2024
 */
public record LoginResponse(UserDto user, String accessToken) {
    @Getter
    private static final String type = "Bearer ";
}
