package com.eruditsioon.ncripterquantumsafe.infrastructure.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HSMKeyLabelMapper {

    public static String mapLabel(String originalLabel) {
        if (originalLabel == null || originalLabel.isEmpty()) {
            return originalLabel;
        }

        // 1. If it already complies with the nShield label constraint (14 lowercase alphanumeric/dash)
        // AND follows our prefix format (t- or p- followed by 12 hex chars)
        if (originalLabel.matches("^[tp]-[a-z0-9]{12}$")) {
            return originalLabel;
        }

        // 2. Special case for backward compatibility with seeded system keys
        if (originalLabel.equals("colombia-vault")) {
            return "t-colombiavaut"; // exactly 14 characters
        }
        if (originalLabel.equals("colombia-audit-kmac")) {
            return "t-colombiaaudt"; // exactly 14 characters
        }

        // 3. Determine prefix and clean input label
        String prefix = "t-"; // default to tenant
        String cleanLabel = originalLabel;

        if (originalLabel.startsWith("tenant:")) {
            prefix = "t-";
            cleanLabel = originalLabel.substring("tenant:".length());
        } else if (originalLabel.startsWith("process:")) {
            prefix = "p-";
            cleanLabel = originalLabel.substring("process:".length());
        } else if (originalLabel.contains("licita") || originalLabel.contains("process") || originalLabel.contains("bid_")) {
            prefix = "p-";
        }

        // Remove any disallowed characters for the hashing input to remain clean
        cleanLabel = cleanLabel.toLowerCase().replaceAll("[^a-z0-9-]", "");

        // 4. Hash clean label using SHA-256 and take first 12 characters of hex
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(cleanLabel.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            // Truncate to 12 chars
            String truncatedHash = hexString.substring(0, 12);
            return prefix + truncatedHash;
        } catch (NoSuchAlgorithmException e) {
            // Fallback: clean and truncate string directly
            String simple = cleanLabel.replaceAll("-", "");
            if (simple.length() > 12) {
                simple = simple.substring(0, 12);
            }
            while (simple.length() < 12) {
                simple = simple + "0";
            }
            return prefix + simple;
        }
    }
}
