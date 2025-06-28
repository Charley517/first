package com.example.logistics.controller;

import com.example.logistics.service.CacheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CacheService cacheService;

    @Test
    void addAndListAddresses() throws Exception {
        when(cacheService.getCommonAddresses(1L)).thenReturn(Arrays.asList("A", "B"));

        mockMvc.perform(post("/api/users/1/addresses").param("address", "A"))
                .andExpect(status().isOk())
                .andExpect(content().string("地址已保存"));
        verify(cacheService).saveCommonAddress(1L, "A");

        mockMvc.perform(get("/api/users/1/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("A"));
        verify(cacheService).getCommonAddresses(1L);
    }
}
