package com.GeoWeather.dto;

import com.GeoWeather.model.Location;
import com.GeoWeather.model.Weather;

public class WeatherResponse {

    private String ip;
    private Location location;
    private Weather weather;
    private String errorMessage; 

    public WeatherResponse(String ip, Location location, Weather weather) {
        this.ip = ip;
        this.location = location;
        this.weather = weather;
    }

    public WeatherResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
