package com.GeoWeather.TestDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.GeoWeather.dto.WeatherResponse;
import com.GeoWeather.model.Location;
import com.GeoWeather.model.Weather;

class WeatherResponseTest {

    @Test
    void testWeatherResponseSuccessConstructor() {
        String ip = "192.168.0.1";
        Location location = new Location("New York", "USA");
        Weather weather = new Weather(25.5, 80, "Sunny");

        WeatherResponse weatherResponse = new WeatherResponse(ip, location, weather);

        assertEquals(ip, weatherResponse.getIp());
        assertEquals(location, weatherResponse.getLocation());
        assertEquals(weather, weatherResponse.getWeather());
        assertNull(weatherResponse.getErrorMessage());
    }
  
    @Test
    void testWeatherResponseErrorConstructor() {
        String errorMessage = "Invalid IP address";

        WeatherResponse weatherResponse = new WeatherResponse(errorMessage);

        assertNull(weatherResponse.getIp());
        assertNull(weatherResponse.getLocation());
        assertNull(weatherResponse.getWeather());
        assertEquals(errorMessage, weatherResponse.getErrorMessage());
    }

    @Test
    void testSetIp() {
        WeatherResponse weatherResponse = new WeatherResponse("192.168.0.1", new Location("London", "UK"), new Weather(15.0, 60, "Cloudy"));
        
        weatherResponse.setIp("10.0.0.1");
        
        assertEquals("10.0.0.1", weatherResponse.getIp());
    }

    @Test
    void testSetLocation() {
        WeatherResponse weatherResponse = new WeatherResponse("192.168.0.1", new Location("Berlin", "Germany"), new Weather(18.0, 65, "Rainy"));
        
        weatherResponse.setLocation(new Location("Paris", "France"));
        
        assertEquals("Paris", weatherResponse.getLocation().getCity());
        assertEquals("France", weatherResponse.getLocation().getCountry());
    }

    @Test
    void testSetWeather() {
        WeatherResponse weatherResponse = new WeatherResponse("192.168.0.1", new Location("Tokyo", "Japan"), new Weather(22.0, 75, "Clear"));
        
        weatherResponse.setWeather(new Weather(30.0, 85, "Hot"));
        
        assertEquals(30.0, weatherResponse.getWeather().getTemperature());
        assertEquals(85, weatherResponse.getWeather().getHumidity());
        assertEquals("Hot", weatherResponse.getWeather().getDescription());
    }

    @Test
    void testSetErrorMessage() {
        WeatherResponse weatherResponse = new WeatherResponse("192.168.0.1", new Location("London", "UK"), new Weather(15.0, 60, "Cloudy"));
        
        weatherResponse.setErrorMessage("Rate limit exceeded");
        
        assertEquals("Rate limit exceeded", weatherResponse.getErrorMessage());
    }
}
