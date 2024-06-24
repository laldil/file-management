package com.example.file_management.exception;

/**
 * @author aldi
 * @since 20.06.2024
 */
public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}
