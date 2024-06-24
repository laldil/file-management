package com.example.file_management.controller.api;

import lombok.Data;

import java.util.Date;

/**
 * @author aldi
 * @since 18.06.2024
 */

@Data
public abstract class ApiResponse {
    private boolean success;
    private String msg = "OK";
    private Date timestamp = new Date();
}