package com.GeoWeather.TestException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.GeoWeather.dto.WeatherResponse;
import com.GeoWeather.exception.GlobalExceptionHandler;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleInvalidIpException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid IP address");
        
        ResponseEntity<WeatherResponse> responseEntity = globalExceptionHandler.handleInvalidIpException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Invalid IP address", responseEntity.getBody().getErrorMessage());
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException exception = new RuntimeException("Service unavailable");
        
        ResponseEntity<WeatherResponse> responseEntity = globalExceptionHandler.handleRuntimeException(exception);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Service unavailable. Please try again later.", responseEntity.getBody().getErrorMessage());
    }
  
    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Generic error");
        
        ResponseEntity<WeatherResponse> responseEntity = globalExceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("An unexpected error occurred.", responseEntity.getBody().getErrorMessage());
    }
}
