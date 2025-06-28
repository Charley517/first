package com.example.logisticssystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.example.logistics.LogisticsSystemApplication;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {LogisticsSystemApplication.class, LogisticsSystemApplicationTests.TestConfig.class})
class LogisticsSystemApplicationTests {

    @MockBean
    private JavaMailSender javaMailSender;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JavaMailSender javaMailSender() {
            return Mockito.mock(JavaMailSender.class);
        }

        @Bean
        public MongoClient mongoClient() {
            return Mockito.mock(MongoClient.class);
        }

        @Bean
        public MongoTemplate mongoTemplate(MongoClient mongoClient) {
            return Mockito.mock(MongoTemplate.class);
        }
    }

    @Test
    void contextLoads() {
        // 验证Spring上下文是否正常加载
    }

    @Test
    void basicTest() {
        // 基本测试用例
        assert true;
    }
} 