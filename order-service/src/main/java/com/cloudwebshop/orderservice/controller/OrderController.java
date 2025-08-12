package com.cloudwebshop.orderservice.controller;

import com.cloudwebshop.orderservice.dto.OrderDto;
import com.cloudwebshop.orderservice.dto.UpdateOrderStatusRequestDto;
import com.cloudwebshop.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private UUID getAuthenticatedUserId() {
        // In a real application, this would come from the Spring Security context
        return UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef");
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {
        return ResponseEntity.ok(orderService.getOrdersForUser(getAuthenticatedUserId()));
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder() {
        OrderDto createdOrder = orderService.createOrderFromCart(getAuthenticatedUserId());
        return ResponseEntity.status(201).body(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable String id, @Valid @RequestBody UpdateOrderStatusRequestDto statusRequest) {
        // The implementation of this method is still a placeholder in the service
        return ResponseEntity.ok(orderService.updateOrderStatus(id, statusRequest.getStatus()));
    }
}
