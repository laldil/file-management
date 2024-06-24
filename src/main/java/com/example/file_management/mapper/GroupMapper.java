package com.example.file_management.mapper;

import com.example.file_management.dto.GroupDto;
import com.example.file_management.dto.GroupMemberDto;
import com.example.file_management.model.GroupEntity;
import com.example.file_management.model.GroupUsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author aldi
 * @since 23.06.2024
 */

@Mapper
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mapping(target = "owner", expression = "java(UserMapper.INSTANCE.mapToDto(entity.getOwner()))")
    @Mapping(target = "clusters", expression = "java(ClusterMapper.INSTANCE.getShortDtoList(entity.getClusters()))")
    @Mapping(target = "groupMembers", expression = "java(getGroupMembers(entity.getGroupUsers()))")
    GroupDto mapToDto(GroupEntity entity);

    default Set<GroupMemberDto> getGroupMembers(Set<GroupUsersEntity> groupMembers) {
        if (groupMembers.isEmpty()) return Collections.emptySet();

        return groupMembers.stream().map(member ->
                new GroupMemberDto(UserMapper.INSTANCE.mapToDto(member.getUser()),
                        member.getMemberRole())).collect(Collectors.toSet());
    }
}
