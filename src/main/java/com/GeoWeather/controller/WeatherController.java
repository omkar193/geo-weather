package com.GeoWeather.controller;

import com.GeoWeather.service.WeatherService;
import com.GeoWeather.util.LoggingUtils;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

import com.GeoWeather.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api")
public class WeatherController {

    private final WeatherService weatherService;
    public final ConcurrentMap<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather-by-ip")
    public ResponseEntity<WeatherResponse> getWeatherByIp(
            @RequestParam(value = "ip", required = false) String ipAddress,
            HttpServletRequest request) {

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        String maskedIp = LoggingUtils.maskIpAddress(ipAddress);  

        Bucket bucket = ipBuckets.computeIfAbsent(ipAddress, this::newBucket);
        if (bucket.tryConsume(1)) {
            logger.info("Rate limit check passed for IP: {}", maskedIp);
            try { 
                WeatherResponse weatherResponse = weatherService.getWeatherByIp(ipAddress);
                return ResponseEntity.ok(weatherResponse);
            } catch (IllegalArgumentException ex) {
                logger.error("Invalid IP address provided: {}", maskedIp);
                return ResponseEntity.badRequest().body(new WeatherResponse("Invalid IP address provided."));
            } catch (RuntimeException ex) {
                logger.error("Service unavailable for IP {}: {}", maskedIp, ex.getMessage());
                return ResponseEntity.status(503).body(new WeatherResponse("Service unavailable. Please try again later."));
            } catch (Exception ex) {
                logger.error("Unexpected error for IP {}: {}", maskedIp, ex.getMessage());
                return ResponseEntity.status(500).body(new WeatherResponse("An unexpected error occurred."));
            }
        } else {
            logger.warn("Rate limit exceeded for IP: {}", maskedIp);
            return ResponseEntity.status(429).body(new WeatherResponse("Too many requests. Please wait a moment."));
        }
    }

    public Bucket newBucket(String ipAddress) {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}
