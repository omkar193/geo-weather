# GeoWeather API

GeoWeather is an API that integrates two powerful external services to deliver real-time weather information based on a given IP address. If no IP address is provided, the API defaults to the client’s IP. It first uses a geolocation API to determine the location from the IP and then fetches the weather data from a weather service API. This seamless integration allows GeoWeather to provide accurate, location-based weather updates with minimal setup and hassle.

## Table of Contents
1. [Features](#features)
2. [API Endpoints](#api-endpoints)
3. [Rate Limiting](#rate-limiting)
4. [Setup Instructions](#setup-instructions)
5. [Environment Configuration](#environment-configuration)
6. [Caching Strategy](#caching-strategy)
7. [Retry Logic](#retry-logic)
8. [Error Handling](#error-handling)
9. [Testing](#testing)
10. [Docker Deployment](#docker-deployment)


## Features
- Fetch weather data based on IP address.
- Rate limiting using Resilience4j.
- Retry logic for API calls.
- Caching with Caffeine for improved performance.
- Circuit breaker and fallback mechanisms.
- Supports both IP-provided and client-determined geolocation.
- Extensive logging for monitoring and debugging.

## API Endpoints

### 1. `GET /api/weather-by-ip`
**Description**: Fetches weather details based on the provided IP address. If no IP is given, it fetches the weather based on the client's IP.

- **Query Parameter**: 
  - `ip` (Optional) - IP address to get the weather information. If not provided, the API will use the client's IP.
  
- **Response**: JSON object containing the IP address, location (city, country), weather details (temperature, humidity, description), and any error messages.

- **Error Codes**:
  - **400 Bad Request**: Invalid IP address.
  - **429 Too Many Requests**: Rate limit exceeded.
  - **503 Service Unavailable**: External services are unavailable.

#### Example 1: With IP parameter
**Request**:
```bash
curl -X GET "http://localhost:8080/api/weather-by-ip?ip=8.8.8.8"
```

**Response**:
```json
{
    "ip": "8.8.8.8",
    "location": {
        "city": "Glenmont",
        "country": "United States"
    },
    "weather": {
        "temperature": -5.86,
        "humidity": 78,
        "description": "scattered clouds"
    },
    "errorMessage": null
}
```

#### Example 2: Without IP parameter
**Request**:
```bash
curl -X GET "http://localhost:8080/api/weather-by-ip"
```

**Response**:
```json
{
    "ip": "0:0:0:0:0:0:0:1",
    "location": {
        "city": "Ghazīpur",
        "country": "India"
    },
    "weather": {
        "temperature": 19.8,
        "humidity": 33,
        "description": "clear sky"
    },
    "errorMessage": null
}
```

### Example:
```bash
curl -X GET "http://localhost:8080/api/weather-by-ip?ip=192.168.1.1"
```

## Rate Limiting
- The application limits the number of requests to **5 per minute** per IP address.
- If the limit is exceeded, a `429 Too Many Requests` response is returned.

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/omkar193/geo-weather.git
   cd geo-weather
   ```
2. Build the project using Maven:
   ```bash
   mvn clean package
   ```
3. Run the application:
   ```bash
   java -jar target/GeoWeather-0.0.1-SNAPSHOT.jar
   ```
4. Ensure you have valid API keys for the geolocation and weather APIs. These should be set in the `application.properties` file.

## Environment Configuration
The application requires the following configurations:

```properties
# Application Name
spring.application.name=GeoWeather

# Geolocation API Configuration
geolocation.api.url=https://api.ipstack.com
geolocation.api.key=dfabdf0f0b7102f0a1c09bb75236cfd0

# Weather API Configuration
weather.api.url=https://api.openweathermap.org/data/2.5/weather
weather.api.key=bc29ecc5f9f64a9ee8edb34e3bc64cb3

# Caching (Caffeine Cache)
spring.cache.cache-names=weatherCache
spring.cache.caffeine.spec=expireAfterWrite=10m

# Server Configuration
server.port=8080

# Retry Configuration 
spring.retry.maxAttempts=3
spring.retry.backoff.delay=2000

# Logging 
logging.level.org.springframework.web=INFO
logging.level.com.GeoWeather=DEBUG

# Rate limit
resilience4j.ratelimiter.instances.weatherService.limitForPeriod=5
resilience4j.ratelimiter.instances.weatherService.limitRefreshPeriod=1m
resilience4j.ratelimiter.instances.weatherService.timeoutDuration=1s
```

## Caching Strategy
The application uses **Caffeine** as an in-memory cache to improve performance by storing recent weather data for **10 minutes**. This reduces the number of external API calls made for frequently requested locations.

## Retry Logic
The application retries failed requests to the external APIs up to **3 times**, with a **backoff delay of 2 seconds** between attempts. This ensures that transient issues are handled gracefully.

## Error Handling
- The application uses custom error responses for invalid IP addresses, API failures, and rate-limiting violations.
- Fallback mechanisms are in place to return default error messages when external services are down or unreachable.

## Testing
- Unit tests cover over **95% of the codebase**, ensuring robust test coverage.
- Test cases include service logic, error handling, and API integration.

Screenshot of test coverage:


<img width="591" alt="image" src="https://github.com/user-attachments/assets/40b18187-2778-40a9-8db7-cd632c12cfb2" />




To run tests, use:
```bash
mvn test
```

## Docker Deployment
The application includes a `Dockerfile` for containerized deployment. You can use Docker Compose to run the application and its dependencies.

### Steps:
1. Build the Docker image:
   ```bash
   docker build -t geo-weather-app .
   ```
2. Run the container:
   ```bash
   docker-compose up
   ```

This will start the GeoWeather application on port **8080**.
