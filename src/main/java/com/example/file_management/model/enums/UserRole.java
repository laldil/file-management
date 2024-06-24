package com.example.file_management.model.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author aldi
 * @since 17.06.2024
 */
public enum UserRole implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
