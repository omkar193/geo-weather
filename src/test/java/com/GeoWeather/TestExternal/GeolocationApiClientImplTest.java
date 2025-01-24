package com.GeoWeather.TestExternal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.GeoWeather.external.GeolocationApiClientImpl;
import com.GeoWeather.model.Location;

public class GeolocationApiClientImplTest {

    private GeolocationApiClientImpl geolocationApiClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        geolocationApiClient = new GeolocationApiClientImpl(restTemplate);
    }

    @Test
    void testGetLocationByIp() throws Exception {
        GeolocationApiClientImpl.GeolocationApiResponse mockedResponse = new GeolocationApiClientImpl.GeolocationApiResponse();
        mockedResponse.setCity("Pune");
        mockedResponse.setCountry("India");

        when(restTemplate.getForObject(anyString(), eq(GeolocationApiClientImpl.GeolocationApiResponse.class)))
                .thenReturn(mockedResponse);

        Field urlField = GeolocationApiClientImpl.class.getDeclaredField("geolocationApiUrl");
        urlField.setAccessible(true);  
        urlField.set(geolocationApiClient, "https://api.example.com"); 

        Location location = geolocationApiClient.getLocationByIp("123.123.123.123");

        assertNotNull(location);
        assertEquals("Pune", location.getCity());
        assertEquals("India", location.getCountry());
    }

    @Test
    void testGetLocationByIp_invalidResponse() throws Exception {
        
        GeolocationApiClientImpl.GeolocationApiResponse mockedResponse = new GeolocationApiClientImpl.GeolocationApiResponse();
        mockedResponse.setCity(null);
        mockedResponse.setCountry(null);

        when(restTemplate.getForObject(anyString(), eq(GeolocationApiClientImpl.GeolocationApiResponse.class)))
                .thenReturn(mockedResponse);

        Field urlField = GeolocationApiClientImpl.class.getDeclaredField("geolocationApiUrl");
        urlField.setAccessible(true);  
        urlField.set(geolocationApiClient, "https://api.example.com");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            geolocationApiClient.getLocationByIp("123.123.123.123");
        });
  
        assertEquals("Failed to fetch location data: Invalid response from geolocation API", exception.getMessage());
     
    }


    @Test
    void testGetLocationByIp_nullIp() throws Exception {

        GeolocationApiClientImpl.GeolocationApiResponse mockedResponse = new GeolocationApiClientImpl.GeolocationApiResponse();
        mockedResponse.setCity("Delhi");
        mockedResponse.setCountry("India");

        when(restTemplate.getForObject(anyString(), eq(GeolocationApiClientImpl.GeolocationApiResponse.class)))
                .thenReturn(mockedResponse);

        Field urlField = GeolocationApiClientImpl.class.getDeclaredField("geolocationApiUrl");
        urlField.setAccessible(true);  
        urlField.set(geolocationApiClient, "https://api.example.com");

        Location location = geolocationApiClient.getLocationByIp(null); 

        assertNotNull(location);
        assertEquals("Delhi", location.getCity());
        assertEquals("India", location.getCountry());
    }
}
