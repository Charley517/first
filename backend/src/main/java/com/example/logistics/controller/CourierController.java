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

    // 根据ID获取快递员
    @GetMapping("/{id}")
    public Courier getCourierById(@PathVariable Long id) {
        return courierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配送员不存在"));
    }

    // 更新快递员位置
    @PutMapping("/{id}/location")
    public Courier updateLocation(@PathVariable Long id, @RequestParam String location) {
        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配送员不存在"));
        courier.setCurrentLocation(location);
        return courierRepository.save(courier);
    }
}
