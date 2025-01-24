package com.GeoWeather.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@SpringBootTest
class CacheConfigTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    void testCacheManagerBeanCreation() {
        assertNotNull(cacheManager, "CacheManager bean should not be null");
    }

    @Test
    void testCacheConfigurationProperties() {
        assertTrue(cacheManager instanceof org.springframework.cache.caffeine.CaffeineCacheManager, 
                   "CacheManager should be an instance of CaffeineCacheManager");

        org.springframework.cache.caffeine.CaffeineCacheManager caffeineCacheManager =
                (org.springframework.cache.caffeine.CaffeineCacheManager) cacheManager;

        assertNotNull(caffeineCacheManager.getCache("weatherCache"), 
                      "Cache 'weatherCache' should exist in the CacheManager");
    }

    @Test
    void testCacheFunctionality() {

        Cache cache = cacheManager.getCache("weatherCache");
        assertNotNull(cache, "Cache 'weatherCache' should not be null");

        String key = "testKey";
        String value = "testValue";
        cache.put(key, value);

        assertEquals(value, cache.get(key, String.class), "Cached value should match the inserted value");

        cache.evict(key);
        assertNull(cache.get(key), "Cache entry should be null after eviction");
    }

}

