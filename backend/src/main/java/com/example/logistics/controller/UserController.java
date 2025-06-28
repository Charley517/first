package com.example.logistics.controller;

import com.example.logistics.entity.User;
import com.example.logistics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 获取所有用户
    @GetMapping("")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 创建用户
    @PostMapping("")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
} 