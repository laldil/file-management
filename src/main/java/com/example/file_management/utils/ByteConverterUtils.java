package com.example.file_management.utils;

/**
 * @author aldi
 * @since 18.06.2024
 */
public class ByteConverterUtils {
    private ByteConverterUtils() {
    }

    public static double bytesToKilobytes(long bytes) {
        return bytes / 1024.0;
    }

    public static double bytesToMegabytes(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }

    public static double bytesToGigabytes(long bytes) {
        return bytes / (1024.0 * 1024.0 * 1024.0);
    }
}
