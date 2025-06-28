package com.example.logistics.service;

import com.example.logistics.entity.LogisticsTrace;
import com.example.logistics.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface LogisticsService {

    /**
     * 添加物流轨迹记录
     * @param orderId 订单ID
     * @param location 位置
     * @param operator 操作员
     * @param status 状态
     * @param description 描述
     */
    void addTrace(Long orderId, String location, String operator, Order.OrderStatus status, String description);

    /**
     * 获取订单的物流轨迹
     * @param orderId 订单ID
     * @return 物流轨迹
     */
    LogisticsTrace getTraceByOrderId(Long orderId);

    /**
     * 获取指定时间范围内的物流轨迹
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 物流轨迹列表
     */
    List<LogisticsTrace> getTracesByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取指定位置的物流轨迹
     * @param location 位置
     * @return 物流轨迹列表
     */
    List<LogisticsTrace> getTracesByLocation(String location);

    /**
     * 获取指定操作员的物流轨迹
     * @param operator 操作员
     * @return 物流轨迹列表
     */
    List<LogisticsTrace> getTracesByOperator(String operator);
} 