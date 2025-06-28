package com.example.logistics.repository;

import com.example.logistics.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
    List<Order> findByCourierId(Long courierId);
} 