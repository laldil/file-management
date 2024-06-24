package com.example.file_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @author aldi
 * @since 23.06.2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupDto {
    private Long id;
    private String name;
    private UserDto owner;
    private String inviteCode;
    private Integer maxUserAmount;
    private Set<GroupMemberDto> groupMembers;
    private List<ClusterShortDto> clusters;
}
