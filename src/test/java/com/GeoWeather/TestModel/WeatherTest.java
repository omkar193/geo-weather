package com.GeoWeather.TestModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.GeoWeather.model.Weather;

class WeatherTest {

    @Test
    void testWeatherConstructor() {
        double temperature = 25.5;
        int humidity = 80;
        String description = "Sunny";  

        Weather weather = new Weather(temperature, humidity, description);

        assertEquals(temperature, weather.getTemperature(), "Temperature should be 25.5");
        assertEquals(humidity, weather.getHumidity(), "Humidity should be 80");
        assertEquals(description, weather.getDescription(), "Description should be 'Sunny'");
    }

    @Test
    void testGetTemperature() {
        Weather weather = new Weather(30.0, 60, "Cloudy");

        double temperature = weather.getTemperature();

        assertEquals(30.0, temperature, "Temperature should be 30.0");
    }

    @Test
    void testSetTemperature() {
        Weather weather = new Weather(20.0, 70, "Rainy");

        weather.setTemperature(18.5);

        assertEquals(18.5, weather.getTemperature(), "Temperature should be updated to 18.5");
    }

    @Test
    void testGetHumidity() {
        Weather weather = new Weather(20.0, 70, "Rainy");

        int humidity = weather.getHumidity();

        assertEquals(70, humidity, "Humidity should be 70");
    }

    @Test
    void testSetHumidity() {
        Weather weather = new Weather(15.0, 50, "Windy");

        weather.setHumidity(60);

        assertEquals(60, weather.getHumidity(), "Humidity should be updated to 60");
    }

    @Test
    void testGetDescription() {
        Weather weather = new Weather(15.0, 50, "Windy");

        String description = weather.getDescription();

        assertEquals("Windy", description, "Description should be 'Windy'");
    }

    @Test
    void testSetDescription() {
        Weather weather = new Weather(10.0, 90, "Foggy");

        weather.setDescription("Clear");

        assertEquals("Clear", weather.getDescription(), "Description should be updated to 'Clear'");
    }

    @Test
    void testWeatherEmptyConstructor() {
        
        Weather weather = new Weather(0.0, 0, null);

        assertEquals(0.0, weather.getTemperature(), "Default temperature should be 0.0");
        assertEquals(0, weather.getHumidity(), "Default humidity should be 0");
        assertNull(weather.getDescription(), "Description should be null");
    }
}

