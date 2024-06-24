package com.example.file_management.repository;

import com.example.file_management.model.ClusterUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author aldi
 * @since 17.06.2024
 */
public interface ClusterUsersRepository extends JpaRepository<ClusterUsersEntity, Long> {
    List<ClusterUsersEntity> findByUserId(Long userId);
}
