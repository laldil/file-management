package com.example.file_management.dto;

import com.example.file_management.model.enums.MemberRole;

/**
 * @author aldi
 * @since 24.06.2024
 */
public record GroupMemberDto(UserDto user, MemberRole memberRole) {
}
