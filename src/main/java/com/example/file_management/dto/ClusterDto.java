package com.example.file_management.dto;

import com.example.file_management.model.enums.ClusterAccessType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author aldi
 * @since 18.06.2024
 */
public record ClusterDto(
        Long id,
        String name,
        ClusterAccessType accessType,
        UserDto owner,
        BigDecimal capacity,
        BigDecimal maxCapacity,
        Date createdDate,
        Date updatedDate,
        List<FileDto> files) {
}
