package com.example.logistics.controller;

import com.example.logistics.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final CacheService cacheService;

    @PostMapping
    public String addAddress(@PathVariable Long userId, @RequestParam String address) {
        cacheService.saveCommonAddress(userId, address);
        return "地址已保存";
    }

    @GetMapping
    public List<String> listAddresses(@PathVariable Long userId) {
        return cacheService.getCommonAddresses(userId);
    }
}
