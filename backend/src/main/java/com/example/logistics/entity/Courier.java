package com.example.logistics.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "courier")
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phone;

    private String currentLocation;

    @Column(columnDefinition = "DOUBLE DEFAULT 0")
    private Double performanceScore;
} 