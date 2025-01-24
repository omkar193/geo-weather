package com.GeoWeather.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoggingUtils {

    public static String maskIpAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return "Unknown IP";
        }
        return ipAddress.replaceAll("(\\d+\\.\\d+\\.\\d+)\\.\\d+", "$1.***");
    }

    public static String hashIpAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return "Unknown IP";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(ipAddress.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing IP address", e);
        }
    }
}
