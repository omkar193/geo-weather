# GeoWeather API

GeoWeather is a simple API that retrieves weather information based on the given IP address. If no IP is provided, the client's IP address is used. This API integrates with external geolocation and weather services.

---

## Table of Contents
1. [API Endpoints](#api-endpoints)
2. [Setup Instructions](#setup-instructions)
3. [API Key Configuration](#api-key-configuration)
4. [Test Execution](#test-execution)
5. [Key Architectural Decisions](#key-architectural-decisions)
6. [Rate Limiting](#rate-limiting)
7. [Error Handling](#error-handling)
8. [Caching](#caching)

---

## API Endpoints

### `GET /api/weather-by-ip`

**Description**: Returns weather information based on the provided IP address. If no IP is supplied, the API uses the client’s IP.

- **URL**: `http://localhost:8080/api/weather-by-ip`
- **Method**: `GET`
- **Query Parameters**:
  - `ip` (optional): The IP address for which to retrieve the weather information. If not provided, the client's IP is used.

#### **Response Format**
- **Success**:
  ```json
  {
    "ipAddress": "8.8.8.8",
    "location": {
      "city": "Mountain View",
      "country": "United States"
    },
    "weather": {
      "temperature": 18.5,
      "description": "Clear sky"
    }
  }
  ```

- **Error Responses**:
  - `400 Bad Request`: Invalid IP.
  - `429 Too Many Requests`: Rate limit exceeded.
  - `503 Service Unavailable`: External service failure.
  - `500 Internal Server Error`: Unexpected errors.

---

## Setup Instructions

### Prerequisites
- **Java 17+**
- **Maven 3+**
- **API Keys**: Obtain API keys for the Geolocation API and the Weather API.
- **Docker** (optional for containerized deployment)

### Steps to Run the Project

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/GeoWeather.git
   cd GeoWeather
   ```

2. **Configure API Keys**:
   - Open the `src/main/resources/application.properties` file and add your API keys:
     ```
     geo.api.key=YOUR_GEOLOCATION_API_KEY
     weather.api.key=YOUR_WEATHER_API_KEY
     ```

3. **Build the Project**:
   ```bash
   mvn clean package
   ```

4. **Run the Application**:
   ```bash
   java -jar target/GeoWeather-1.0-SNAPSHOT.jar
   ```

5. **Run the Application using Docker** (Optional):
   - Build and run the Docker container using `docker-compose`:
     ```bash
     docker-compose up
     ```

---

## API Key Configuration

To run the application, you'll need to configure two external API keys:

- **Geolocation API**: Provides location data based on an IP address.
- **Weather API**: Fetches weather details for a given city.

Add these keys to the `application.properties` file:
```
geo.api.key=YOUR_GEOLOCATION_API_KEY
weather.api.key=YOUR_WEATHER_API_KEY
```

---

## Test Execution

To run the tests:

1. **Run Unit Tests using Maven**:
   - The project comes with unit tests to validate the API logic. You can execute the tests using the following Maven command:
   ```bash
   mvn test
   ```

2. **Integration Testing**:
   - Ensure that external APIs are functional by running integration tests (if applicable).

3. **Postman or CURL Testing**:
   - You can use Postman or `curl` to manually test the API:
     ```bash
     curl -X GET "http://localhost:8080/api/weather-by-ip?ip=8.8.8.8"
     ```

---

## Key Architectural Decisions

1. **Rate Limiting**:
   - The API uses `Bucket4j` to enforce a rate limit of **5 requests per minute** per IP address.

2. **Retry Logic & Circuit Breaker**:
   - External API calls are wrapped with Resilience4j’s retry and circuit breaker mechanisms to ensure reliability during failures.

3. **Caching**:
   - Weather data is cached using Spring’s in-memory caching to reduce the number of requests to the external Weather API and improve performance.

4. **Error Handling**:
   - The API uses structured error handling to deal with various edge cases, including invalid IP addresses, rate-limit violations, and external API failures.

---

## Rate Limiting

- The API enforces a rate limit of **5 requests per minute** per IP address.
- If the rate limit is exceeded, the API responds with `429 Too Many Requests`.

---

## Error Handling

- **400 Bad Request**: Invalid IP Address provided.
- **429 Too Many Requests**: Rate limit exceeded.
- **503 Service Unavailable**: External service failure.
- **500 Internal Server Error**: For unexpected server errors.

---

## Caching

- Weather data is cached using Spring's default in-memory cache for each IP to minimize the number of requests to external APIs.
- Cache TTL is based on the frequency of API calls to ensure up-to-date weather information.

---
