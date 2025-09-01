package com.cloudwebshop.remotetests;

import com.cloudwebshop.orderservice.OrderServiceApplication;
import com.cloudwebshop.userservice.UserServiceApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserServiceRemoteTest {

    private static ConfigurableApplicationContext userServiceContext;
    private static ConfigurableApplicationContext orderServiceContext;

    @BeforeAll
    public static void startServices() {
        userServiceContext = SpringApplication.run(UserServiceApplication.class, "--server.port=0");
        orderServiceContext = SpringApplication.run(OrderServiceApplication.class, "--server.port=0");
    }

    @AfterAll
    public static void stopServices() {
        if (userServiceContext != null) {
            userServiceContext.close();
        }
        if (orderServiceContext != null) {
            orderServiceContext.close();
        }
    }

    @Test
    public void testCreateOrderForUser() {
        assertNotNull(userServiceContext);
        assertNotNull(orderServiceContext);
    }
}
