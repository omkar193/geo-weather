package com.GeoWeather.TestUtils;

import org.junit.jupiter.api.Test;

import com.GeoWeather.util.LoggingUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


class LoggingUtilsTest {

	@Test
	void testMaskIpAddress_ValidIp() {

	    String ipAddress = "192.168.0.1";
	    String maskedIp = LoggingUtils.maskIpAddress(ipAddress);

	    String expectedMaskedIp = "192.168.0.***";

	    assertEquals(expectedMaskedIp, maskedIp);
	}
  

    @Test
    void testMaskIpAddress_InvalidIp() {

        String ipAddress = "";
        String maskedIp = LoggingUtils.maskIpAddress(ipAddress);

        assertEquals("Unknown IP", maskedIp);
    }

    @Test
    void testMaskIpAddress_NullIp() {

        String ipAddress = null;
        String maskedIp = LoggingUtils.maskIpAddress(ipAddress);

        assertEquals("Unknown IP", maskedIp);
    }

    @Test
    void testHashIpAddress_ValidIp() {

        String ipAddress = "192.168.0.1";
        String hashedIp = LoggingUtils.hashIpAddress(ipAddress);

        assertNotNull(hashedIp);
        assertEquals(64, hashedIp.length()); 
    }

    @Test
    void testHashIpAddress_InvalidIp() {
        String ipAddress = "";
        String hashedIp = LoggingUtils.hashIpAddress(ipAddress);

        assertEquals("Unknown IP", hashedIp);
    }

    @Test
    void testHashIpAddress_NullIp() {

        String ipAddress = null;
        String hashedIp = LoggingUtils.hashIpAddress(ipAddress);

        assertEquals("Unknown IP", hashedIp);
    }

    @Test
    void testHashIpAddress_ExceptionHandling() {

        try {
            MessageDigest.getInstance("NonExistentAlgorithm");
            fail("Expected an exception to be thrown");
        } catch (NoSuchAlgorithmException e) {
            assertTrue(e.getMessage().contains("Algorithm"));
        }  
    }
}
