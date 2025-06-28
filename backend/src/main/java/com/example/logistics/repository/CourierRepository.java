package com.example.logistics.repository;

import com.example.logistics.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    // 可以添加自定义查询方法
} 