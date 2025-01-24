package com.GeoWeather.TestService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.GeoWeather.dto.WeatherResponse;
import com.GeoWeather.external.GeolocationApiClient;
import com.GeoWeather.external.WeatherApiClient;
import com.GeoWeather.model.Location;
import com.GeoWeather.model.Weather;
import com.GeoWeather.service.WeatherService;

class WeatherServiceTest {

    @Mock
    private GeolocationApiClient geolocationApiClient;

    @Mock
    private WeatherApiClient weatherApiClient;

    @InjectMocks
    private WeatherService weatherService;

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWeatherByIp_Success() {
        String ipAddress = "192.168.0.1";
        String city = "New York";
        String country = "USA";
        Location location = new Location(city, country);
        Weather weather = new Weather(20.0, 50, "Clear");

        when(geolocationApiClient.getLocationByIp(ipAddress)).thenReturn(location);
        when(weatherApiClient.getWeatherByCity(city)).thenReturn(weather);

        WeatherResponse response = weatherService.getWeatherByIp(ipAddress);

        assertNotNull(response);
        assertEquals(city, response.getLocation().getCity());
        assertEquals(country, response.getLocation().getCountry());
        assertEquals(20.0, response.getWeather().getTemperature());
        assertEquals(50, response.getWeather().getHumidity());
        assertEquals("Clear", response.getWeather().getDescription());

        verify(geolocationApiClient).getLocationByIp(ipAddress);
        verify(weatherApiClient).getWeatherByCity(city);
    }
  

    @Test
    void testGetWeatherByIp_InvalidLocation() {
        String ipAddress = "192.168.0.1";

        when(geolocationApiClient.getLocationByIp(ipAddress)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weatherService.getWeatherByIp(ipAddress);
        });

        assertEquals("Failed to retrieve location information for IP: " + ipAddress, exception.getMessage());

        verify(geolocationApiClient).getLocationByIp(ipAddress);
        verifyNoInteractions(weatherApiClient); 
    }

    @Test
    void testGetWeatherByIp_InvalidWeather() {
        String ipAddress = "192.168.0.1";
        String city = "New York";
        Location location = new Location(city, "USA");

        when(geolocationApiClient.getLocationByIp(ipAddress)).thenReturn(location);
        when(weatherApiClient.getWeatherByCity(city)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weatherService.getWeatherByIp(ipAddress);
        });

        assertEquals("Failed to retrieve weather information for city: " + city, exception.getMessage());

        verify(geolocationApiClient).getLocationByIp(ipAddress);
        verify(weatherApiClient).getWeatherByCity(city);
    }

    @Test
    void testFallbackWeatherResponse() {
        String ipAddress = "192.168.0.1";
        Throwable throwable = new RuntimeException("Service temporarily unavailable");

        WeatherResponse response = weatherService.fallbackWeatherResponse(ipAddress, throwable);

        assertNotNull(response);
        assertEquals("Service temporarily unavailable. Please try again later.", response.getErrorMessage());
    }

}
