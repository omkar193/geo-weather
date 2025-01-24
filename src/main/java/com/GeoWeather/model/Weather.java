package com.GeoWeather.model;

public class Weather {

    private double temperature;
    private int humidity;
    private String description;

    public Weather(double temperature, int humidity, String description) {
        this.temperature = temperature;
        this.humidity = humidity; 
        this.description = description;
    }

    public double getTemperature() {
        return temperature; 
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

