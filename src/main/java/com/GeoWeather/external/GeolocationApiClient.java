package com.GeoWeather.external;

import com.GeoWeather.model.Location;

public interface GeolocationApiClient {
	Location getLocationByIp(String ipAddress);
}
