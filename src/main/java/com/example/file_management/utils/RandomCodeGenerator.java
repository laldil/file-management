package com.example.file_management.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author aldi
 * @since 23.06.2024
 */
public class RandomCodeGenerator {
    private final static SecureRandom random = new SecureRandom();
    private final static int CODE_LENGTH = 6;
    private final static char[] NUMBERS = "0123456789".toCharArray();

    public static String generateCode() {
        return generateCode(CODE_LENGTH);
    }

    public static String generateCode(int length) {
        char[] code = new char[length];

        for (int i = 0; i < length; i++) {
            code[i] = NUMBERS[random.nextInt(NUMBERS.length)];
        }

        for (int i = 0; i < code.length; i++) {
            int randomPosition = random.nextInt(code.length);
            char temp = code[i];
            code[i] = code[randomPosition];
            code[randomPosition] = temp;
        }

        return new String(code);
    }

    public static String generateInviteCode(Long id) {
        return "INV-" + id + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
