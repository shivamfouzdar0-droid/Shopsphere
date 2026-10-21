package com.shopsphere.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility for hashing and verifying user passwords using standard cryptographic SHA-256.
 */
public class PasswordUtil {

    private PasswordUtil() {
        // Utility class
    }

    /**
     * Hashes a plaintext password using SHA-256.
     * @param plainPassword plain text password
     * @return 64-character lowercase hex string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available in JDK", e);
        }
    }

    /**
     * Verifies whether a plain password matches a hashed password.
     * @param plainPassword plain text password
     * @param hashedPassword stored SHA-256 hex string
     * @return true if matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        String computedHash = hashPassword(plainPassword);
        return computedHash.equalsIgnoreCase(hashedPassword);
    }
}
