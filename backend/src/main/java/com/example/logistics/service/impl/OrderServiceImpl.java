package com.example.logistics.service.impl;

import com.example.logistics.entity.Courier;
import com.example.logistics.entity.Order;
import com.example.logistics.entity.User;
import com.example.logistics.repository.CourierRepository;
import com.example.logistics.repository.OrderRepository;
import com.example.logistics.repository.UserRepository;
import com.example.logistics.service.NotificationService;
import com.example.logistics.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CourierRepository courierRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public Order createOrder(Long senderId, Long receiverId, Long courierId, Double weight, Double distance) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("寄件人不存在"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("收件人不存在"));
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("配送员不存在"));

        double fee = calculateFee(weight, distance);

        Order order = new Order();
        order.setSender(sender);
        order.setReceiver(receiver);
        order.setCourier(courier);
        order.setWeight(weight);
        order.setDistance(distance);
        order.setFee(fee);
        order.setStatus(Order.OrderStatus.已揽件);
        order.setCreatedTime(LocalDateTime.now());
        order.setUpdatedTime(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        
        // 发送订单创建通知
        notificationService.sendOrderStatusNotification(
            savedOrder.getId(),
            savedOrder.getStatus().name(),
            savedOrder.getReceiver().getUsername()
        );

        return savedOrder;
    }

    @Override
    @Cacheable(value = "orders", key = "'all'")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Cacheable(value = "orders", key = "#orderId")
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "#orderId")
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = getOrderById(orderId);
        Order.OrderStatus oldStatus = order.getStatus();
        order.setStatus(status);
        order.setUpdatedTime(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);

        // 发送状态变更通知
        try {
            notificationService.sendOrderStatusNotification(
                savedOrder.getId(),
                savedOrder.getStatus().name(),
                savedOrder.getReceiver().getUsername()
            );
            log.info("订单状态更新通知发送成功：订单ID={}, 原状态={}, 新状态={}",
                    orderId, oldStatus, status);
        } catch (Exception e) {
            log.error("订单状态更新通知发送失败：订单ID={}, 状态={}", orderId, status, e);
            // 通知发送失败不影响主流程
        }

        return savedOrder;
    }

    @Override
    @Cacheable(value = "orders", key = "'user_' + #userId")
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findBySenderIdOrReceiverId(userId, userId);
    }

    @Override
    @Cacheable(value = "orders", key = "'courier_' + #courierId")
    public List<Order> getOrdersByCourierId(Long courierId) {
        return orderRepository.findByCourierId(courierId);
    }

    @Override
    public double calculateFee(double weight, double distance) {
        // 基础运费 + 重量费用 + 距离费用
        return 5 + weight * 0.8 + distance * 0.5;
    }
} 