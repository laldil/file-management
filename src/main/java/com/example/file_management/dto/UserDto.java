package com.example.file_management.dto;

import com.example.file_management.model.enums.UserRole;

/**
 * @author aldi
 * @since 18.06.2024
 */
public record UserDto(Long id, String name, String email, UserRole role) {
}
