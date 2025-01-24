package com.GeoWeather.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testRestTemplateBeanCreation() {
        
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        assertNotNull(restTemplate, "RestTemplate bean should not be null");
    }

    @Test
    void testRestTemplateSingletonScope() {
       
        RestTemplate restTemplate1 = context.getBean(RestTemplate.class);
        RestTemplate restTemplate2 = context.getBean(RestTemplate.class);
        assertSame(restTemplate1, restTemplate2, "RestTemplate bean should be a singleton");
    }

    @Test
    void testRestTemplateUsability() {
      
        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        assertDoesNotThrow(() -> {
            String response = restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/1", String.class);
            assertNotNull(response, "Response from the REST API should not be null");
        }, "RestTemplate should be usable for HTTP calls");
    }
}

