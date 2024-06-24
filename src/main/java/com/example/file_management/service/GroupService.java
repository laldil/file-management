package com.example.file_management.service;

import com.example.file_management.dto.CreateClusterRequest;
import com.example.file_management.dto.CreateGroupRequest;
import com.example.file_management.dto.GroupDto;
import com.example.file_management.exception.PermissionDeniedException;
import com.example.file_management.mapper.GroupMapper;
import com.example.file_management.model.GroupEntity;
import com.example.file_management.model.GroupUsersEntity;
import com.example.file_management.model.enums.MemberRole;
import com.example.file_management.repository.ClusterRepository;
import com.example.file_management.repository.GroupRepository;
import com.example.file_management.repository.UserRepository;
import com.example.file_management.utils.RandomCodeGenerator;
import com.example.file_management.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

/**
 * @author aldi
 * @since 23.06.2024
 */

@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ClusterRepository clusterRepository;

    private final ClusterService clusterService;

    public GroupDto create(CreateGroupRequest request) {
        var group = new GroupEntity();

        group.setName(request.name());
        group.setOwner(userRepository.findById(SecurityUtils.getCurrentId()).orElseThrow(() -> new EntityNotFoundException("User not found")));
        group.setMaxUserAmount(20);
        var savedGroup = groupRepository.save(group);

        group.setInviteCode(RandomCodeGenerator.generateInviteCode(savedGroup.getId()));
        savedGroup = groupRepository.save(savedGroup);

        return GroupMapper.INSTANCE.mapToDto(savedGroup);
    }

    public GroupDto addMemberByEmail(String email, Long groupId) {
        var memberToAdd = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        var currentUserId = SecurityUtils.getCurrentId();
        var group = groupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("Group not found"));

        if (group.getGroupUsers().stream().noneMatch(groupMember -> groupMember.getUser().getId().equals(currentUserId))
                && !group.getOwner().getId().equals(currentUserId)) {
            throw new PermissionDeniedException("Permission denied");
        }
        group.getGroupUsers().add(new GroupUsersEntity(group, memberToAdd, MemberRole.READER));

        var savedGroup = groupRepository.save(group);
        return GroupMapper.INSTANCE.mapToDto(savedGroup);
    }

    public GroupDto joinByCode(String code) {
        var group = groupRepository.findByInviteCode(code)
                .orElseThrow(() -> new BadCredentialsException("%s is unknown".formatted(code)));
        var currentUser = userRepository.findById(SecurityUtils.getCurrentId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        group.getGroupUsers().add(new GroupUsersEntity(group, currentUser, MemberRole.READER));
        var savedGroup = groupRepository.save(group);
        return GroupMapper.INSTANCE.mapToDto(savedGroup);
    }

    public GroupDto createCluster(Long groupId, CreateClusterRequest request) {
        var group = groupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("Group not found"));

        validatePermission(group);

        var clusterDto = clusterService.create(request);
        var clusterEntity = clusterRepository.findById(clusterDto.id()).get();
        group.getClusters().add(clusterEntity);
        var savedGroup = groupRepository.save(group);

        return GroupMapper.INSTANCE.mapToDto(savedGroup);
    }

    public GroupDto getGroup(Long id) {
        var group = groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Group not found"));

        validatePermission(group);
        return GroupMapper.INSTANCE.mapToDto(group);
    }

    private void validatePermission(GroupEntity group) {
        var currentUserId = SecurityUtils.getCurrentId();

        if (!group.getOwner().getId().equals(currentUserId)
                && group.getGroupUsers().stream().noneMatch(gu -> gu.getUser().getId().equals(currentUserId))) {
            throw new PermissionDeniedException("Permission denied");
        }
    }
}
