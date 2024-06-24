package com.example.file_management.service;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author aldi
 * @since 18.06.2024
 */

@RequiredArgsConstructor
@Service
public class MinioService {
    private final MinioClient minioClient;

    @SneakyThrows
    public void saveFile(InputStream inputStream, String filename, String clusterName) {
        try {
            createBucketIfNotExists(clusterName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(clusterName)
                .object(filename)
                .build());
    }

    @SneakyThrows
    private void createBucketIfNotExists(String bucketName) {
        var found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    @SneakyThrows
    public InputStream getFileStream(String fileName, String clusterName) {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(clusterName)
                .object(fileName)
                .build());
    }

    @SneakyThrows
    public void removeFile(String filename, String clusterName) {
        minioClient.removeObject(
                RemoveObjectArgs.builder().bucket(clusterName).object(filename).build());
    }

}
