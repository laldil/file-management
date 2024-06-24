package com.example.file_management.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author aldi
 * @since 18.06.2024
 */
public record FileDto(Long id, Date createdDate, String originalName, BigDecimal size, UserDto owner) {
}
