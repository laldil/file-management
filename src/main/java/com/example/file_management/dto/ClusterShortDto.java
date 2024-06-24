package com.example.file_management.dto;

import com.example.file_management.model.enums.ClusterAccessType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author aldi
 * @since 18.06.2024
 */
public record ClusterShortDto(
        Long id,
        String name,
        ClusterAccessType accessType,
        UserDto owner,
        Date updatedDate,
        BigDecimal capacity,
        BigDecimal maxCapacity) {
}
