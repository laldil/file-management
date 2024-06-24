package com.example.file_management.repository;

import com.example.file_management.model.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author aldi
 * @since 17.06.2024
 */
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByInviteCode(String inviteCode);
}
