package com.example.logisticssystem;

import com.example.logistics.LogisticsSystemApplication;
import com.example.logistics.entity.Order;
import com.example.logistics.repository.CourierRepository;
import com.example.logistics.repository.OrderRepository;
import com.example.logistics.repository.UserRepository;
import com.example.logistics.service.NotificationService;
import com.example.logistics.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = LogisticsSystemApplication.class)
@EnableCaching
class OrderServiceCachingTest {

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CourierRepository courierRepository;
    @MockBean
    private NotificationService notificationService;

    @Autowired
    private OrderService orderService;

    @Test
    void getOrderByIdUsesCache() {
        Order order = new Order();
        order.setId(1L);
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order first = orderService.getOrderById(1L);
        Order second = orderService.getOrderById(1L);

        assertSame(first, second);
        verify(orderRepository, times(1)).findById(1L);
    }
}
