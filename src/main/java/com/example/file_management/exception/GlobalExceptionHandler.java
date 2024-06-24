package com.example.file_management.exception;

import com.example.file_management.controller.api.ApiEmptyResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

/**
 * @author aldi
 * @since 18.06.2024
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiEmptyResponse> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiEmptyResponse.failed(e.getMessage()));
    }

    @ExceptionHandler(value = {BadCredentialsException.class, FileUploadException.class})
    public ResponseEntity<ApiEmptyResponse> handleBadCredentialException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiEmptyResponse.failed(e.getMessage()));
    }

    @ExceptionHandler(value = MultipartException.class)
    public ResponseEntity<ApiEmptyResponse> handleMultipartException(MultipartException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiEmptyResponse.failed(e.getMessage()));
    }

    @ExceptionHandler(value = PermissionDeniedException.class)
    public ResponseEntity<ApiEmptyResponse> handlePermissionDeniedException(PermissionDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiEmptyResponse.failed(e.getMessage()));
    }
}
