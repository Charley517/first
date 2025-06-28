package com.example.logistics.service;

import com.example.logistics.entity.LogisticsTrace;
import com.example.logistics.entity.Order;
import com.example.logistics.entity.User;
import com.example.logistics.repository.LogisticsTraceRepository;
import com.example.logistics.repository.OrderRepository;
import com.example.logistics.service.impl.LogisticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogisticsServiceImplTest {
    @Mock
    LogisticsTraceRepository traceRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    NotificationService notificationService;

    @InjectMocks
    LogisticsServiceImpl logisticsService;

    @Test
    void addTraceShouldSendNotifications() {
        Long orderId = 1L;
        Order order = new Order();
        User receiver = new User();
        receiver.setUsername("receiver");
        order.setReceiver(receiver);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(traceRepository.findByOrderId(orderId)).thenReturn(Optional.empty());

        logisticsService.addTrace(orderId, "loc", "op", Order.OrderStatus.运输中, "desc");

        verify(notificationService).sendOrderStatusNotification(orderId, "运输中", "receiver");
        verify(notificationService).sendLocationUpdateNotification(orderId, "loc", "receiver");
        ArgumentCaptor<LogisticsTrace> captor = ArgumentCaptor.forClass(LogisticsTrace.class);
        verify(traceRepository).save(captor.capture());
        LogisticsTrace saved = captor.getValue();
        assertThat(saved.getOrderId()).isEqualTo(orderId);
        assertThat(saved.getTraces()).hasSize(1);
    }
}
