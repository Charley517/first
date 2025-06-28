package com.example.logistics.service.impl;

import com.example.logistics.service.CacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String COMMON_ADDRESS_KEY = "common_address:";
    private static final String ROUTE_CACHE_KEY = "route:";
    private static final String GEOCODE_CACHE_KEY = "geocode:";
    private static final int MAX_COMMON_ADDRESSES = 10;
    private static final long DEFAULT_EXPIRE_TIME = 24 * 60 * 60; // 24小时

    @Override
    public void saveCommonAddress(Long userId, String address) {
        String key = COMMON_ADDRESS_KEY + userId;
        redisTemplate.opsForList().leftPush(key, address);
        redisTemplate.opsForList().trim(key, 0, MAX_COMMON_ADDRESSES - 1);
        redisTemplate.expire(key, DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    @Override
    public List<String> getCommonAddresses(Long userId) {
        String key = COMMON_ADDRESS_KEY + userId;
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    public void saveRouteCache(String key, Map<String, Object> route) {
        try {
            String routeJson = objectMapper.writeValueAsString(route);
            redisTemplate.opsForValue().set(ROUTE_CACHE_KEY + key, routeJson, DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("保存路线缓存失败", e);
        }
    }

    @Override
    public Map<String, Object> getRouteCache(String key) {
        try {
            String routeJson = redisTemplate.opsForValue().get(ROUTE_CACHE_KEY + key);
            if (routeJson != null) {
                return objectMapper.readValue(routeJson, new TypeReference<Map<String, Object>>() {});
            }
        } catch (JsonProcessingException e) {
            log.error("获取路线缓存失败", e);
        }
        return null;
    }

    @Override
    public void saveGeocodeCache(String address, Map<String, Object> geocode) {
        try {
            String geocodeJson = objectMapper.writeValueAsString(geocode);
            redisTemplate.opsForValue().set(GEOCODE_CACHE_KEY + address, geocodeJson, DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("保存地理编码缓存失败", e);
        }
    }

    @Override
    public Map<String, Object> getGeocodeCache(String address) {
        try {
            String geocodeJson = redisTemplate.opsForValue().get(GEOCODE_CACHE_KEY + address);
            if (geocodeJson != null) {
                return objectMapper.readValue(geocodeJson, new TypeReference<Map<String, Object>>() {});
            }
        } catch (JsonProcessingException e) {
            log.error("获取地理编码缓存失败", e);
        }
        return null;
    }

    @Override
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void setExpire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }
} 