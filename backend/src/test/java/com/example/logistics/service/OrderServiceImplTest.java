package com.example.logistics.service;

import com.example.logistics.entity.Courier;
import com.example.logistics.entity.Order;
import com.example.logistics.entity.User;
import com.example.logistics.repository.CourierRepository;
import com.example.logistics.repository.OrderRepository;
import com.example.logistics.repository.UserRepository;
import com.example.logistics.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CourierRepository courierRepository;
    @Mock
    NotificationService notificationService;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    void updateStatusShouldIncreasePerformanceWhenDelivered() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(Order.OrderStatus.运输中);
        Courier courier = new Courier();
        courier.setId(2L);
        courier.setPerformanceScore(2.0);
        order.setCourier(courier);
        User receiver = new User();
        receiver.setUsername("recv");
        order.setReceiver(receiver);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
        when(courierRepository.save(any(Courier.class))).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.updateOrderStatus(1L, Order.OrderStatus.已签收);

        assertThat(result.getStatus()).isEqualTo(Order.OrderStatus.已签收);
        assertThat(courier.getPerformanceScore()).isEqualTo(3.0);
        verify(notificationService).sendOrderStatusNotification(1L, "已签收", "recv");
    }
}
