package com.cloudwebshop.remotetests;

import com.cloudwebshop.orderservice.OrderServiceApplication;
import com.cloudwebshop.userservice.UserServiceApplication;
import com.cloudwebshop.userservice.dto.UserDto;
import lombok.Data;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceRemoteTest {

    private static ConfigurableApplicationContext userServiceContext;
    private static ConfigurableApplicationContext orderServiceContext;
    private final RestTemplate restTemplate = new RestTemplate();

    private final String userServiceUrl = "http://localhost:8080/api/v1/users";
    private final String orderServiceUrl = "http://localhost:8081/api/v1";

    @BeforeAll
    static void startServices() {
        userServiceContext = SpringApplication.run(UserServiceApplication.class, "--server.port=8080");
        orderServiceContext = SpringApplication.run(OrderServiceApplication.class, "--server.port=8081");
    }

    @AfterAll
    static void stopServices() {
        userServiceContext.close();
        orderServiceContext.close();
    }

    @Test
    void testCreateOrderForUser() {
        // First, get user profile to have a valid user id.
        ResponseEntity<UserDto> userResponse = restTemplate.getForEntity(userServiceUrl + "/profile", UserDto.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        UUID userId = userResponse.getBody().getId();

        // Add an item to the cart
        AddItemRequestDto addItemRequest = new AddItemRequestDto();
        addItemRequest.setProductId(UUID.randomUUID());
        addItemRequest.setQuantity(2);

        ResponseEntity<OrderDto> addItemResponse = restTemplate.postForEntity(orderServiceUrl + "/cart/items", addItemRequest, OrderDto.class);
        assertThat(addItemResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Create an order from the cart
        ResponseEntity<OrderDto> createOrderResponse = restTemplate.postForEntity(orderServiceUrl + "/orders", null, OrderDto.class);
        assertThat(createOrderResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        OrderDto createdOrder = createOrderResponse.getBody();
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getUserId()).isEqualTo(userId);
        assertThat(createdOrder.getStatus()).isEqualTo("NEW");
    }


    // Inner classes for DTOs
    @Data
    static class OrderDto {
        private UUID id;
        private String orderNumber;
        private UUID userId;
        private String status;
        private BigDecimal totalAmount;
        private String currency;
        private String shippingAddress;
        private String billingAddress;
        private List<OrderItemDto> items;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    static class OrderItemDto {
        private UUID id;
        private UUID productId;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }

    @Data
    static class AddItemRequestDto {
        private UUID productId;
        private Integer quantity;
    }
}
