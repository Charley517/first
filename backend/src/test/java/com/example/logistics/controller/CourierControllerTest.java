package com.example.logistics.controller;

import com.example.logistics.entity.Courier;
import com.example.logistics.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourierController.class)
class CourierControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierRepository courierRepository;

    @Test
    void getCourierById() throws Exception {
        Courier courier = new Courier();
        courier.setId(1L);
        courier.setName("张三");
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));

        mockMvc.perform(get("/api/couriers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("张三"));
    }

    @Test
    void updateLocation() throws Exception {
        Courier courier = new Courier();
        courier.setId(1L);
        courier.setName("张三");
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(courierRepository.save(any(Courier.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(put("/api/couriers/1/location").param("location", "loc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentLocation").value("loc"));
    }
}
