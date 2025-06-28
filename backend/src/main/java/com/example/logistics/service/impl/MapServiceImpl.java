package com.example.logistics.service.impl;

import com.example.logistics.service.CacheService;
import com.example.logistics.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

    private final RestTemplate restTemplate;
    private final CacheService cacheService;

    @Value("${amap.key}")
    private String amapKey;

    @Value("${amap.base-url}")
    private String baseUrl;

    @Override
    public Map<String, Object> getDrivingRoute(String origin, String destination) {
        String cacheKey = origin + ":" + destination;
        Map<String, Object> cachedRoute = cacheService.getRouteCache(cacheKey);
        if (cachedRoute != null) {
            log.info("从缓存获取路线规划：origin={}, destination={}", origin, destination);
            return cachedRoute;
        }

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/direction/driving")
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("key", amapKey)
                .build()
                .toUriString();

        log.info("请求驾车路线规划：origin={}, destination={}", origin, destination);
        Map<String, Object> route = restTemplate.getForObject(url, Map.class);
        if (route != null && "1".equals(route.get("status"))) {
            cacheService.saveRouteCache(cacheKey, route);
        }
        return route;
    }

    @Override
    public Map<String, Object> getGeocode(String address) {
        Map<String, Object> cachedGeocode = cacheService.getGeocodeCache(address);
        if (cachedGeocode != null) {
            log.info("从缓存获取地理编码：address={}", address);
            return cachedGeocode;
        }

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/geocode/geo")
                .queryParam("address", address)
                .queryParam("key", amapKey)
                .build()
                .toUriString();

        log.info("请求地理编码：address={}", address);
        Map<String, Object> geocode = restTemplate.getForObject(url, Map.class);
        if (geocode != null && "1".equals(geocode.get("status"))) {
            cacheService.saveGeocodeCache(address, geocode);
        }
        return geocode;
    }

    @Override
    public Map<String, Object> getRegeocode(String location) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/geocode/regeo")
                .queryParam("location", location)
                .queryParam("key", amapKey)
                .build()
                .toUriString();

        log.info("请求逆地理编码：location={}", location);
        return restTemplate.getForObject(url, Map.class);
    }

    @Override
    public double calculateDistance(String origin, String destination) {
        Map<String, Object> route = getDrivingRoute(origin, destination);
        if (route != null && "1".equals(route.get("status"))) {
            Object routeObj = route.get("route");
            if (routeObj instanceof Map<?, ?> routeMap) {
                Object pathsObj = routeMap.get("paths");
                if (pathsObj instanceof List<?> pathsList && !pathsList.isEmpty()) {
                    Object firstPath = pathsList.get(0);
                    if (firstPath instanceof Map<?, ?> pathMap) {
                        Object distanceObj = pathMap.get("distance");
                        if (distanceObj != null) {
                            try {
                                return Double.parseDouble(distanceObj.toString());
                            } catch (NumberFormatException e) {
                                log.error("距离值格式错误：{}", distanceObj);
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }
} 