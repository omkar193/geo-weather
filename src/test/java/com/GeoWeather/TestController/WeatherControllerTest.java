package com.GeoWeather.TestController;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.GeoWeather.controller.WeatherController;
import com.GeoWeather.dto.WeatherResponse;
import com.GeoWeather.service.WeatherService;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private WeatherController weatherController;

    private Bucket mockBucket;

    @BeforeEach
    void setup() {
      
        mockBucket = Bucket.builder()
                .addLimit(io.github.bucket4j.Bandwidth.classic(5, io.github.bucket4j.Refill.greedy(5, Duration.ofMinutes(1))))
                .build();
    }

    @Test
    void testGetWeatherByIp_Success() throws Exception {
        String ipAddress = "192.168.0.1";
        WeatherResponse mockResponse = new WeatherResponse(ipAddress, null, null);

        lenient().when(request.getRemoteAddr()).thenReturn(ipAddress);
        when(weatherService.getWeatherByIp(ipAddress)).thenReturn(mockResponse);

        ResponseEntity<WeatherResponse> responseEntity = weatherController.getWeatherByIp(ipAddress, request);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(ipAddress, responseEntity.getBody().getIp());
        verify(weatherService).getWeatherByIp(ipAddress);
    }

    @Test
    void testGetWeatherByIp_RateLimitExceeded() {
        String ipAddress = "192.168.0.1";
        Bucket mockRateLimitedBucket = spy(weatherController.newBucket(ipAddress));
        mockRateLimitedBucket.tryConsume(5); 
        
        weatherController.ipBuckets.put(ipAddress, mockRateLimitedBucket);

        lenient().when(request.getRemoteAddr()).thenReturn(ipAddress);

        ResponseEntity<WeatherResponse> responseEntity = weatherController.getWeatherByIp(ipAddress, request);

        assertEquals(429, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals("Too many requests. Please wait a moment.", responseEntity.getBody().getErrorMessage());
    }

    @Test
    void testGetWeatherByIp_InvalidIpAddress() {
        String ipAddress = "invalid_ip";
        lenient().when(request.getRemoteAddr()).thenReturn(ipAddress);
        when(weatherService.getWeatherByIp(ipAddress)).thenThrow(new IllegalArgumentException("Invalid IP address provided."));

        ResponseEntity<WeatherResponse> responseEntity = weatherController.getWeatherByIp(ipAddress, request);

        assertEquals(400, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals("Invalid IP address provided.", responseEntity.getBody().getErrorMessage());
        verify(weatherService).getWeatherByIp(ipAddress);
    }

    @Test
    void testGetWeatherByIp_ServiceUnavailable() {
        String ipAddress = "192.168.0.1";
        lenient().when(request.getRemoteAddr()).thenReturn(ipAddress);
        when(weatherService.getWeatherByIp(ipAddress)).thenThrow(new RuntimeException("Service unavailable."));

        ResponseEntity<WeatherResponse> responseEntity = weatherController.getWeatherByIp(ipAddress, request);

        assertEquals(503, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals("Service unavailable. Please try again later.", responseEntity.getBody().getErrorMessage());
        verify(weatherService).getWeatherByIp(ipAddress);
    }

    @Test
    void testGetWeatherByIp_UnexpectedError() {
        String ipAddress = "192.168.0.1";
        lenient().when(request.getRemoteAddr()).thenReturn(ipAddress);

        when(weatherService.getWeatherByIp(ipAddress)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<WeatherResponse> responseEntity = weatherController.getWeatherByIp(ipAddress, request);

        assertEquals(503, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals("Service unavailable. Please try again later.", responseEntity.getBody().getErrorMessage());
        verify(weatherService).getWeatherByIp(ipAddress);
    }



    @Test
    void testGetWeatherByIp_NoIpProvided_UsesRemoteAddr() throws Exception {
        String ipAddress = "192.168.0.1";
        when(request.getRemoteAddr()).thenReturn(ipAddress);

        WeatherResponse mockResponse = new WeatherResponse(ipAddress, null, null);
        when(weatherService.getWeatherByIp(ipAddress)).thenReturn(mockResponse);

        ResponseEntity<WeatherResponse> responseEntity = weatherController.getWeatherByIp(null, request);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(ipAddress, responseEntity.getBody().getIp());
        verify(weatherService).getWeatherByIp(ipAddress);
    }

    @Test
    void testNewBucket_CreatesNewBucketWithLimits() {
        String ipAddress = "192.168.0.1";
        Bucket bucket = weatherController.newBucket(ipAddress);

        assertNotNull(bucket);
        assertTrue(bucket.tryConsume(1)); 
    }
}
