package com.example.file_management.repository;

import com.example.file_management.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author aldi
 * @since 17.06.2024
 */
public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
