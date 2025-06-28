package com.example.logistics.service;

import com.example.logistics.entity.Order;

import java.util.List;

public interface OrderService {
    /**
     * 创建订单
     * @param senderId 寄件人ID
     * @param receiverId 收件人ID
     * @param courierId 配送员ID
     * @param weight 重量
     * @param distance 距离
     * @return 创建的订单
     */
    Order createOrder(Long senderId, Long receiverId, Long courierId, Double weight, Double distance);

    /**
     * 获取所有订单
     * @return 订单列表
     */
    List<Order> getAllOrders();

    /**
     * 根据ID获取订单
     * @param orderId 订单ID
     * @return 订单信息
     */
    Order getOrderById(Long orderId);

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @return 更新后的订单
     */
    Order updateOrderStatus(Long orderId, Order.OrderStatus status);

    /**
     * 获取用户的订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> getOrdersByUserId(Long userId);

    /**
     * 获取配送员的订单列表
     * @param courierId 配送员ID
     * @return 订单列表
     */
    List<Order> getOrdersByCourierId(Long courierId);

    /**
     * 计算订单运费
     * @param weight 重量
     * @param distance 距离
     * @return 运费
     */
    double calculateFee(double weight, double distance);

    /**
     * 删除订单
     * @param orderId 订单ID
     */
    void deleteOrder(Long orderId);
}
