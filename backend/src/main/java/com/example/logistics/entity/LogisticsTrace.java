package com.example.logistics.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "logistics_trace")
public class LogisticsTrace {

    @Id
    private String id;

    private Long orderId;

    private List<TraceNode> traces;

    @Data
    public static class TraceNode {
        private LocalDateTime timestamp;
        private String location;
        private String operator;
        private String status;
        private String description;
    }
} 