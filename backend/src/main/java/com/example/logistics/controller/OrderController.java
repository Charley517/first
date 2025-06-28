package com.example.logistics.controller;

import com.example.logistics.entity.Order;
import com.example.logistics.service.OrderService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.logistics.entity.User;
import com.example.logistics.entity.Courier;
import com.example.logistics.repository.UserRepository;
import com.example.logistics.repository.CourierRepository;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourierRepository courierRepository;

    // 创建订单
    @PostMapping("")
    public Order createOrder(@RequestBody CreateOrderRequest req) {
        System.out.println("收到创建订单请求：" + req);
        return orderService.createOrder(req.getSenderId(), req.getReceiverId(), req.getCourierId(), req.getWeight(), req.getDistance());
    }

    // 查询所有订单
    @GetMapping("")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // 根据ID查询订单
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // 获取所有用户
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 获取所有快递员
    @GetMapping("/couriers")
    public List<Courier> getAllCouriers() {
        return courierRepository.findAll();
    }

    // 创建用户（寄件人/收件人）
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // 创建快递员
    @PostMapping("/couriers")
    public Courier createCourier(@RequestBody Courier courier) {
        return courierRepository.save(courier);
    }

    // 删除订单
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @Data
    public static class CreateOrderRequest {
        private Long senderId;
        private Long receiverId;
        private Long courierId;
        private Double weight;
        private Double distance;
    }
} 