package com.GeoWeather.external;

import com.GeoWeather.model.Weather;

public interface WeatherApiClient {
	Weather getWeatherByCity(String city);
}
