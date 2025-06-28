package com.example.logistics;

import com.example.logistics.entity.Courier;
import com.example.logistics.entity.Order;
import com.example.logistics.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class E2ETests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void testCreateAndFetchOrder() {
        User sender = new User();
        sender.setUsername("sender");
        sender.setPassword("pwd");
        sender.setRole(User.UserRole.SENDER);
        ResponseEntity<User> senderResp = rest.postForEntity("/api/orders/users", sender, User.class);
        assertThat(senderResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long senderId = senderResp.getBody().getId();

        User receiver = new User();
        receiver.setUsername("receiver");
        receiver.setPassword("pwd");
        receiver.setRole(User.UserRole.RECEIVER);
        ResponseEntity<User> recvResp = rest.postForEntity("/api/orders/users", receiver, User.class);
        Long receiverId = recvResp.getBody().getId();

        Courier courier = new Courier();
        courier.setName("c1");
        ResponseEntity<Courier> courResp = rest.postForEntity("/api/orders/couriers", courier, Courier.class);
        Long courierId = courResp.getBody().getId();

        Map<String, Object> req = new HashMap<>();
        req.put("senderId", senderId);
        req.put("receiverId", receiverId);
        req.put("courierId", courierId);
        req.put("weight", 1.0);
        req.put("distance", 2.0);

        ResponseEntity<Order> orderResp = rest.postForEntity("/api/orders", req, Order.class);
        assertThat(orderResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long orderId = orderResp.getBody().getId();

        ResponseEntity<Order> getResp = rest.getForEntity("/api/orders/" + orderId, Order.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResp.getBody().getId()).isEqualTo(orderId);
    }
}
