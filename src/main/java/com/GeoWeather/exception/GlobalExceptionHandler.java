package com.GeoWeather.exception;

import com.GeoWeather.dto.WeatherResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<WeatherResponse> handleInvalidIpException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new WeatherResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    } 

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<WeatherResponse> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(new WeatherResponse("Service unavailable. Please try again later."), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WeatherResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new WeatherResponse("An unexpected error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

