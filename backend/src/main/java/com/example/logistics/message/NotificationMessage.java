package com.example.logistics.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    private Long orderId;
    private String orderNumber;
    private String status;
    private String target;  // 目标用户，如手机号或钉钉账号
    private String type;    // 通知类型：SMS/EMAIL/DINGTALK
    private String content; // 通知内容
    private LocalDateTime createTime;
    private String operator; // 操作人
    private String location; // 当前位置
} 