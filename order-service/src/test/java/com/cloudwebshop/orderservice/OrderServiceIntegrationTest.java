package com.cloudwebshop.orderservice;

import com.cloudwebshop.orderservice.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void contextLoads() {
        assertThat(orderMapper).isNotNull();
    }
}