package com.GeoWeather.external;

import com.GeoWeather.model.Location;
import com.GeoWeather.util.LoggingUtils;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeolocationApiClientImpl implements GeolocationApiClient {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(GeolocationApiClientImpl.class);

    @Value("${geolocation.api.url}")
    private String geolocationApiUrl;

    @Value("${geolocation.api.key}")
    private String apiKey;

    public GeolocationApiClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate; 
    } 

    @Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @Override
    public Location getLocationByIp(String ipAddress) {
        try {
            String url;
            if (ipAddress == null || ipAddress.isEmpty() || ipAddress.equals("0:0:0:0:0:0:0:1") || ipAddress.equals("127.0.0.1")) {
                url = String.format("%s/check?access_key=%s", geolocationApiUrl, apiKey);
            } else {
                url = String.format("%s/%s?access_key=%s", geolocationApiUrl, ipAddress, apiKey);
            }

            String maskedIp = LoggingUtils.maskIpAddress(ipAddress);  
            logger.info("Calling Geolocation API for IP: {}", maskedIp);

            GeolocationApiResponse response = restTemplate.getForObject(url, GeolocationApiResponse.class);

            logger.info("Geolocation API Response for IP {}: {}", maskedIp, response);

            if (response != null && response.getCity() != null && response.getCountry() != null) {
                return new Location(response.getCity(), response.getCountry());
            } else {
                throw new RuntimeException("Invalid response from geolocation API");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch location data: " + e.getMessage(), e);
        }
    }


    @VisibleForTesting
	public 
    static class GeolocationApiResponse {
        private String city;
        @JsonProperty("country_name")
        private String country_name;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country_name;
        }

        public void setCountry(String country) {
            this.country_name = country;
        }

        @Override
        public String toString() {
            return "GeolocationApiResponse [city=" + city + ", country=" + country_name + "]";
        }
    }
}
