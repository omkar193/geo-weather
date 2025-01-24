package com.GeoWeather.TestModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.GeoWeather.model.Location;

class LocationTest {

    @Test
    void testLocationConstructor() {
        String city = "New York";
        String country = "USA";

        Location location = new Location(city, country);

        assertEquals(city, location.getCity(), "City should be 'New York'");
        assertEquals(country, location.getCountry(), "Country should be 'USA'");
    }
  
    @Test
    void testGetCity() {
        Location location = new Location("London", "UK");

        String city = location.getCity();

        assertEquals("London", city, "City should be 'London'");
    }

    @Test
    void testSetCity() {
        Location location = new Location("Paris", "France");

        location.setCity("Berlin");

        assertEquals("Berlin", location.getCity(), "City should be updated to 'Berlin'");
    }

    @Test
    void testGetCountry() {
        Location location = new Location("Tokyo", "Japan");

        String country = location.getCountry();

        assertEquals("Japan", country, "Country should be 'Japan'");
    }

    @Test
    void testSetCountry() {
        Location location = new Location("Sydney", "Australia");

        location.setCountry("New Zealand");

        assertEquals("New Zealand", location.getCountry(), "Country should be updated to 'New Zealand'");
    }

    @Test
    void testLocationEmptyConstructor() {
        Location location = new Location(null, null);

        assertNull(location.getCity(), "City should be null");
        assertNull(location.getCountry(), "Country should be null");
    }
}

