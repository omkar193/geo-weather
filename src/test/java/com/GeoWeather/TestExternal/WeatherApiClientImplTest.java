package com.GeoWeather.TestExternal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.GeoWeather.external.WeatherApiClientImpl;
import com.GeoWeather.model.Weather;

public class WeatherApiClientImplTest {

    private WeatherApiClientImpl weatherApiClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        weatherApiClient = new WeatherApiClientImpl(restTemplate);
    }

    @Test
    void testGetWeatherByCity() throws Exception {
      
        WeatherApiClientImpl.WeatherApiResponse mockedResponse = new WeatherApiClientImpl.WeatherApiResponse();
        WeatherApiClientImpl.WeatherApiResponse.Main main = new WeatherApiClientImpl.WeatherApiResponse.Main();
        main.setTemp(25.5);
        main.setHumidity(60);
        mockedResponse.setMain(main);
  
        WeatherApiClientImpl.WeatherApiResponse.WeatherDescription weatherDescription = new WeatherApiClientImpl.WeatherApiResponse.WeatherDescription();
        weatherDescription.setDescription("clear sky");
        mockedResponse.setWeather(java.util.Collections.singletonList(weatherDescription));

        when(restTemplate.getForObject(anyString(), eq(WeatherApiClientImpl.WeatherApiResponse.class)))
                .thenReturn(mockedResponse);

        Field urlField = WeatherApiClientImpl.class.getDeclaredField("weatherApiUrl");
        urlField.setAccessible(true);
        urlField.set(weatherApiClient, "https://api.weather.com"); 

        Weather weather = weatherApiClient.getWeatherByCity("Pune");

        assertNotNull(weather);
        assertEquals(25.5, weather.getTemperature());
        assertEquals(60, weather.getHumidity());
        assertEquals("clear sky", weather.getDescription());
    }

}

