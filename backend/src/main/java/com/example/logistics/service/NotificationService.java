package com.example.logistics.service;

import com.example.logistics.message.NotificationMessage;

public interface NotificationService {
    /**
     * 发送通知
     * @param message 通知消息
     */
    void sendNotification(NotificationMessage message);

    /**
     * 发送订单状态变更通知
     * @param orderId 订单ID
     * @param status 新状态
     * @param target 目标用户
     */
    void sendOrderStatusNotification(Long orderId, String status, String target);

    /**
     * 发送配送员位置更新通知
     * @param orderId 订单ID
     * @param location 位置
     * @param target 目标用户
     */
    void sendLocationUpdateNotification(Long orderId, String location, String target);
} 