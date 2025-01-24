package com.GeoWeather.util;

public class IpAddressValidator {

    public static boolean isValidIpAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return false;  
        }

        String ipPattern = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b";
        return ipAddress.matches(ipPattern);
    }
}

