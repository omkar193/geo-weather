package com.GeoWeather.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.GeoWeather.model.Weather;
import com.google.common.annotations.VisibleForTesting;

@Service
public class WeatherApiClientImpl implements WeatherApiClient {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(WeatherApiClientImpl.class);

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherApiClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "weatherCache", key = "#city", unless = "#result == null")
    @Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @Override
    public Weather getWeatherByCity(String city) {
        try {
            String url = String.format("%s?q=%s&appid=%s&units=metric", weatherApiUrl, city, apiKey);

            logger.info("Weather API URL for city {}: {}", city, url); 
           
            WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);

            if (response != null && response.getMain() != null && response.getWeather() != null && !response.getWeather().isEmpty()) {
                return new Weather(
                        response.getMain().getTemp(),
                        response.getMain().getHumidity(),
                        response.getWeather().get(0).getDescription()
                );
            } else {
                throw new RuntimeException("Invalid response from weather API");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage(), e);
        }
    }

    @VisibleForTesting
	public 
    static class WeatherApiResponse {
        private Main main;
        private java.util.List<WeatherDescription> weather;

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main; 
        }

        public java.util.List<WeatherDescription> getWeather() {
            return weather;
        }

        public void setWeather(java.util.List<WeatherDescription> weather) {
            this.weather = weather;
        }

        public static class Main {
            private double temp;
            private int humidity;

            public double getTemp() {
                return temp;
            }

            public void setTemp(double temp) {
                this.temp = temp;
            }

            public int getHumidity() {
                return humidity;
            }

            public void setHumidity(int humidity) {
                this.humidity = humidity;
            }
        }

        public static class WeatherDescription {
            private String description;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }
    }
}
