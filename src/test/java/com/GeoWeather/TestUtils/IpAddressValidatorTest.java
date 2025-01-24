package com.GeoWeather.TestUtils;

import org.junit.jupiter.api.Test;

import com.GeoWeather.util.IpAddressValidator;

import static org.junit.jupiter.api.Assertions.*;

class IpAddressValidatorTest {

    @Test  
    void testValidIpAddress() {

        assertTrue(IpAddressValidator.isValidIpAddress("192.168.0.1"));
    }

    @Test
    void testValidIpAddressWithLeadingZeroes() {
  
        assertTrue(IpAddressValidator.isValidIpAddress("192.168.01.01"));
    }

    @Test
    void testInvalidIpAddressWithLetters() {

        assertFalse(IpAddressValidator.isValidIpAddress("192.168.a.b"));
    }

    @Test
    void testInvalidIpAddressWithTooManyOctets() {
 
        assertFalse(IpAddressValidator.isValidIpAddress("192.168.0.1.1"));
    }

    @Test
    void testInvalidIpAddressWithTooFewOctets() {

        assertFalse(IpAddressValidator.isValidIpAddress("192.168.0"));
    }

    @Test
    void testInvalidIpAddressWithExcessiveNumbers() {

    	assertTrue(IpAddressValidator.isValidIpAddress("999.999.999.999"));
    }

    @Test
    void testNullIpAddress() {

        assertFalse(IpAddressValidator.isValidIpAddress(null));
    }

    @Test
    void testEmptyIpAddress() {

        assertFalse(IpAddressValidator.isValidIpAddress(""));
    }

    @Test
    void testIpAddressWithSpaces() {

        assertFalse(IpAddressValidator.isValidIpAddress("192. 168.0.1"));
    }

    @Test
    void testValidLocalIpAddress() {

        assertTrue(IpAddressValidator.isValidIpAddress("127.0.0.1"));
    }
}

