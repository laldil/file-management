package com.example.file_management.service;

import com.example.file_management.exception.FileUploadException;
import com.example.file_management.model.FileEntity;
import com.example.file_management.repository.FileRepository;
import com.example.file_management.repository.UserRepository;
import com.example.file_management.utils.ByteConverterUtils;
import com.example.file_management.utils.FileUtils;
import com.example.file_management.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author aldi
 * @since 18.06.2024
 */

@RequiredArgsConstructor
@Service
public class FileService {
    private final MinioService minioService;

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public FileEntity upload(MultipartFile file, String bucketName) {
        if (file == null || file.getOriginalFilename() == null) {
            throw new RuntimeException("File upload failed");
        }

        var filename = FileUtils.generateFilename(file);
        try (InputStream inputStream = file.getInputStream()) {
            minioService.saveFile(inputStream, filename, bucketName);
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage());
        }

        var fileEntity = FileEntity.builder()
                .name(filename)
                .originalName(file.getOriginalFilename())
                .owner(userRepository.findById(SecurityUtils.getCurrentId()).get())
                .size(BigDecimal.valueOf(ByteConverterUtils.bytesToMegabytes(file.getSize())))
                .build();
        return fileRepository.save(fileEntity);
    }

    public List<FileEntity> uploadList(List<MultipartFile> files, String bucketName) {
        return files.stream().map(file -> upload(file, bucketName)).toList();
    }

    public Pair<String, Resource> getFile(Long id, String clusterName) {
        var fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("File with id %d not found", id)));

        return Pair.of(fileEntity.getOriginalName(), new InputStreamResource(minioService.getFileStream(fileEntity.getName(), clusterName)));
    }

    public Boolean delete(Long id, String clusterName) {
        var file = fileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("File not found"));
        minioService.removeFile(file.getName(), clusterName);
        fileRepository.deleteById(id);
        return true;
    }

}
