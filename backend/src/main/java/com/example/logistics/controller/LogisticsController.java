package com.example.logistics.controller;

import com.example.logistics.entity.LogisticsTrace;
import com.example.logistics.entity.Order;
import com.example.logistics.service.LogisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logistics")
@RequiredArgsConstructor
public class LogisticsController {

    private final LogisticsService logisticsService;

    /**
     * 添加物流轨迹
     */
    @PostMapping("/trace")
    public ResponseEntity<String> addTrace(
            @RequestParam @NotNull Long orderId,
            @RequestParam @NotBlank String location,
            @RequestParam @NotBlank String operator,
            @RequestParam @NotNull Order.OrderStatus status,
            @RequestParam(required = false) String description) {
        logisticsService.addTrace(orderId, location, operator, status, description);
        return ResponseEntity.ok("物流轨迹添加成功");
    }

    /**
     * 获取订单物流轨迹
     */
    @GetMapping("/trace/{orderId}")
    public ResponseEntity<LogisticsTrace> getTraceByOrderId(
            @PathVariable @NotNull Long orderId) {
        return ResponseEntity.ok(logisticsService.getTraceByOrderId(orderId));
    }

    /**
     * 获取时间范围内的物流轨迹
     */
    @GetMapping("/trace/time-range")
    public ResponseEntity<List<LogisticsTrace>> getTracesByTimeRange(
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(logisticsService.getTracesByTimeRange(startTime, endTime));
    }

    /**
     * 获取指定位置的物流轨迹
     */
    @GetMapping("/trace/location/{location}")
    public ResponseEntity<List<LogisticsTrace>> getTracesByLocation(
            @PathVariable @NotBlank String location) {
        return ResponseEntity.ok(logisticsService.getTracesByLocation(location));
    }

    /**
     * 获取指定操作员的物流轨迹
     */
    @GetMapping("/trace/operator/{operator}")
    public ResponseEntity<List<LogisticsTrace>> getTracesByOperator(
            @PathVariable @NotBlank String operator) {
        return ResponseEntity.ok(logisticsService.getTracesByOperator(operator));
    }
} 