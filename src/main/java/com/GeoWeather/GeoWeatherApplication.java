package com.GeoWeather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GeoWeatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoWeatherApplication.class, args);
	}

}
