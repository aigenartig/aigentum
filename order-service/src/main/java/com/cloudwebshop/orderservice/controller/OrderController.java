package com.cloudwebshop.orderservice.controller;

import com.cloudwebshop.orderservice.dto.OrderDto;
import com.cloudwebshop.orderservice.dto.UpdateOrderStatusRequestDto;
import com.cloudwebshop.orderservice.mapper.OrderMapper;
import com.cloudwebshop.orderservice.model.Order;
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
    private final OrderMapper orderMapper;

    private UUID getAuthenticatedUserId() {
        return UUID.fromString("11111111-1111-1111-1111-111111111111");
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {
        List<Order> orderEntities = orderService.getOrdersForUser(getAuthenticatedUserId());
        return ResponseEntity.ok(orderMapper.toOrderDtoList(orderEntities));
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder() {
        Order createdOrder = orderService.createOrderFromCart(getAuthenticatedUserId());
        return ResponseEntity.status(201).body(orderMapper.toOrderDto(createdOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id) {
        Order orderEntity = orderService.getOrderById(id);
        return ResponseEntity.ok(orderMapper.toOrderDto(orderEntity));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable UUID id, @Valid @RequestBody UpdateOrderStatusRequestDto statusRequest) {
        Order updatedOrder = orderService.updateOrderStatus(id, statusRequest.getStatus());
        return ResponseEntity.ok(orderMapper.toOrderDto(updatedOrder));
    }
}
