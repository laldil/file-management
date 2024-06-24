package com.example.file_management.utils;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

/**
 * @author aldi
 * @since 18.06.2024
 */
public class FileUtils {
    public static String generateFilename(MultipartFile file) {
        String extension = getExtension(file);
        Long time = new Date().getTime();
        return time + "_" + UUID.randomUUID() + "." + extension;
    }

    public static String getExtension(MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new BadCredentialsException("Filename is empty");
        }

        return file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }
}