package com.example.logistics.service.impl;

import com.example.logistics.entity.LogisticsTrace;
import com.example.logistics.entity.Order;
import com.example.logistics.repository.LogisticsTraceRepository;
import com.example.logistics.service.LogisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogisticsServiceImpl implements LogisticsService {

    private final LogisticsTraceRepository traceRepository;

    @Override
    @CacheEvict(value = "logisticsTraces", key = "#orderId")
    public void addTrace(Long orderId, String location, String operator, Order.OrderStatus status, String description) {
        LogisticsTrace trace = traceRepository.findByOrderId(orderId)
                .orElseGet(() -> {
                    LogisticsTrace newTrace = new LogisticsTrace();
                    newTrace.setOrderId(orderId);
                    newTrace.setTraces(new ArrayList<>());
                    return newTrace;
                });

        LogisticsTrace.TraceNode node = new LogisticsTrace.TraceNode();
        node.setLocation(location);
        node.setOperator(operator);
        node.setStatus(status.name());
        node.setDescription(description);
        node.setTimestamp(LocalDateTime.now());

        trace.getTraces().add(node);
        traceRepository.save(trace);
    }

    @Override
    @Cacheable(value = "logisticsTraces", key = "#orderId")
    public LogisticsTrace getTraceByOrderId(Long orderId) {
        return traceRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("物流轨迹不存在"));
    }

    @Override
    @Cacheable(value = "logisticsTraces", key = "'timeRange_' + #startTime + '_' + #endTime")
    public List<LogisticsTrace> getTracesByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return traceRepository.findByTimeRange(startTime, endTime);
    }

    @Override
    @Cacheable(value = "logisticsTraces", key = "'location_' + #location")
    public List<LogisticsTrace> getTracesByLocation(String location) {
        return traceRepository.findByLocation(location);
    }

    @Override
    @Cacheable(value = "logisticsTraces", key = "'operator_' + #operator")
    public List<LogisticsTrace> getTracesByOperator(String operator) {
        return traceRepository.findByOperator(operator);
    }
} 