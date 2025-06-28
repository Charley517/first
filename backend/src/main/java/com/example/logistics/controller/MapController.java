package com.example.logistics.controller;

import com.example.logistics.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    /**
     * 获取驾车路线规划
     */
    @GetMapping("/route")
    public ResponseEntity<Map<String, Object>> getRoute(
            @RequestParam String origin,
            @RequestParam String destination) {
        return ResponseEntity.ok(mapService.getDrivingRoute(origin, destination));
    }

    /**
     * 获取地址解析结果
     */
    @GetMapping("/geocode")
    public ResponseEntity<Map<String, Object>> getGeocode(
            @RequestParam String address) {
        return ResponseEntity.ok(mapService.getGeocode(address));
    }

    /**
     * 获取逆地理编码结果
     */
    @GetMapping("/regeocode")
    public ResponseEntity<Map<String, Object>> getRegeocode(
            @RequestParam String location) {
        return ResponseEntity.ok(mapService.getRegeocode(location));
    }

    /**
     * 计算两点间距离
     */
    @GetMapping("/distance")
    public ResponseEntity<Double> calculateDistance(
            @RequestParam String origin,
            @RequestParam String destination) {
        return ResponseEntity.ok(mapService.calculateDistance(origin, destination));
    }
} 