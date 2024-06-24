package com.example.file_management.dto;

import com.example.file_management.model.enums.ClusterAccessType;
import jakarta.validation.constraints.NotNull;

/**
 * @author aldi
 * @since 18.06.2024
 */

public record CreateClusterRequest(
        @NotNull(message = "name must be provided")
        String name,

        @NotNull(message = "access type must be provided")
        ClusterAccessType accessType) {
}
