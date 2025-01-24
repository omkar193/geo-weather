
package com.GeoWeather.service;

import com.GeoWeather.dto.WeatherResponse;
import com.GeoWeather.external.GeolocationApiClient;
import com.GeoWeather.external.WeatherApiClient;
import com.GeoWeather.model.Location;
import com.GeoWeather.model.Weather;
import com.GeoWeather.util.LoggingUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WeatherService {

    private final GeolocationApiClient geolocationApiClient;
    private final WeatherApiClient weatherApiClient;

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Autowired  
    public WeatherService(GeolocationApiClient geolocationApiClient, WeatherApiClient weatherApiClient) {
        this.geolocationApiClient = geolocationApiClient;
        this.weatherApiClient = weatherApiClient;
    }

    @Retry(name = "weatherService", fallbackMethod = "fallbackWeatherResponse")
    @CircuitBreaker(name = "weatherService", fallbackMethod = "fallbackWeatherResponse")
    @Cacheable(value = "weatherCache", key = "#ipAddress", unless = "#result == null")
    public WeatherResponse getWeatherByIp(String ipAddress) {
       
        String maskedIp = LoggingUtils.maskIpAddress(ipAddress);

        logger.info("Attempting to fetch weather for IP: {}", maskedIp);

        Location location = geolocationApiClient.getLocationByIp(ipAddress);

        logger.info("Fetched Location for IP {}: {}", maskedIp, location);

        if (location == null || location.getCity() == null || location.getCountry() == null) {
            throw new RuntimeException("Failed to retrieve location information for IP: " + ipAddress);
        }

        Weather weather = weatherApiClient.getWeatherByCity(location.getCity());

        logger.info("Fetched Weather for city {}: {}", location.getCity(), weather);

        if (weather == null) {
            throw new RuntimeException("Failed to retrieve weather information for city: " + location.getCity());
        }

        return new WeatherResponse(ipAddress, location, weather);
    }

    public WeatherResponse fallbackWeatherResponse(String ipAddress, Throwable t) {
        String maskedIp = LoggingUtils.maskIpAddress(ipAddress);

        logger.error("Fallback executed for IP: {} due to: {}", maskedIp, t.getMessage());
        return new WeatherResponse("Service temporarily unavailable. Please try again later.");
    }
}

