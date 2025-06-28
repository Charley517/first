package com.example.logistics.controller;

import com.example.logistics.entity.Courier;
import com.example.logistics.repository.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/couriers")
public class CourierController {

    @Autowired
    private CourierRepository courierRepository;

    // 获取所有快递员
    @GetMapping("")
    public List<Courier> getAllCouriers() {
        return courierRepository.findAll();
    }

    // 创建快递员
    @PostMapping("")
    public Courier createCourier(@RequestBody Courier courier) {
        return courierRepository.save(courier);
    }
} 