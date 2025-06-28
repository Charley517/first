package com.example.logistics.listener;

import com.example.logistics.config.RabbitMQConfig;
import com.example.logistics.message.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class NotificationListener {

    private final RestTemplate restTemplate;
    private final JavaMailSender mailSender;

    @Value("${notification.dingtalk.webhook}")
    private String dingtalkWebhook;

    @Value("${notification.email.from}")
    private String emailFrom;

    public NotificationListener(RestTemplate restTemplate, JavaMailSender mailSender) {
        this.restTemplate = restTemplate;
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleNotification(NotificationMessage message) {
        try {
            log.info("📩 收到通知消息：{}", message);

            switch (message.getType().toUpperCase()) {
                case "SMS":
                    sendSmsNotification(message);
                    break;
                case "EMAIL":
                    sendEmailNotification(message);
                    break;
                case "DINGTALK":
                    sendDingtalkNotification(message);
                    break;
                default:
                    log.warn("未知的通知类型：{}", message.getType());
            }
        } catch (Exception e) {
            log.error("处理通知消息失败：", e);
            // 这里可以添加重试逻辑或发送到死信队列
        }
    }

    private void sendSmsNotification(NotificationMessage message) {
        // 模拟短信发送
        log.info("📱 发送短信通知：{} -> {}", message.getTarget(), message.getContent());
    }

    private void sendEmailNotification(NotificationMessage message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        
        helper.setFrom(emailFrom);
        helper.setTo(message.getTarget());
        helper.setSubject("物流订单状态更新通知");
        helper.setText(message.getContent(), true);
        
        mailSender.send(mimeMessage);
        log.info("📧 发送邮件通知：{} -> {}", message.getTarget(), message.getContent());
    }

    private void sendDingtalkNotification(NotificationMessage message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("msgtype", "text");
        
        Map<String, String> text = new HashMap<>();
        text.put("content", message.getContent());
        body.put("text", text);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        
        restTemplate.postForEntity(dingtalkWebhook, request, String.class);
        log.info("🔔 发送钉钉通知：{}", message.getContent());
    }
} 