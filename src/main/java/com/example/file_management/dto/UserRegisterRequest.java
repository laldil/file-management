package com.example.file_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * @author aldi
 * @since 17.06.2024
 */
public record UserRegisterRequest(
        @NotNull(message = "name must be provided")
        String name,

        @NotNull(message = "email must be provided")
        @Email(message = "email is not valid")
        String email,

        @NotNull(message = "password must be provided")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                message = "Minimum eight characters, at least one letter and one number")
        String password
) {
}
