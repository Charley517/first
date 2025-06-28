package com.example.logistics.service.impl;

import com.example.logistics.config.RabbitMQConfig;
import com.example.logistics.message.NotificationMessage;
import com.example.logistics.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendNotification(NotificationMessage message) {
        message.setCreateTime(LocalDateTime.now());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
    }

    @Override
    public void sendOrderStatusNotification(Long orderId, String status, String target) {
        NotificationMessage message = NotificationMessage.builder()
                .orderId(orderId)
                .status(status)
                .target(target)
                .type("SMS")
                .content(String.format("您的订单 %d 状态已更新为：%s", orderId, status))
                .createTime(LocalDateTime.now())
                .build();
        sendNotification(message);
    }

    @Override
    public void sendLocationUpdateNotification(Long orderId, String location, String target) {
        NotificationMessage message = NotificationMessage.builder()
                .orderId(orderId)
                .target(target)
                .type("SMS")
                .content(String.format("您的订单 %d 配送员当前位置：%s", orderId, location))
                .location(location)
                .createTime(LocalDateTime.now())
                .build();
        sendNotification(message);
    }
} 