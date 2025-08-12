package com.cloudwebshop.orderservice.controller;

import com.cloudwebshop.orderservice.dto.AddItemRequestDto;
import com.cloudwebshop.orderservice.dto.OrderDto;
import com.cloudwebshop.orderservice.dto.UpdateCartItemRequestDto;
import com.cloudwebshop.orderservice.mapper.OrderMapper;
import com.cloudwebshop.orderservice.model.Order;
import com.cloudwebshop.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    private UUID getAuthenticatedUserId() {
        return UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef");
    }

    @GetMapping
    public ResponseEntity<OrderDto> getCart() {
        Order cartEntity = orderService.getCart(getAuthenticatedUserId());
        return ResponseEntity.ok(orderMapper.toOrderDto(cartEntity));
    }

    @PostMapping("/items")
    public ResponseEntity<OrderDto> addItemToCart(@Valid @RequestBody AddItemRequestDto itemRequest) {
        Order updatedCart = orderService.addItemToCart(
                getAuthenticatedUserId(),
                itemRequest.getProductId(),
                itemRequest.getQuantity()
        );
        return ResponseEntity.ok(orderMapper.toOrderDto(updatedCart));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<OrderDto> updateCartItem(@PathVariable UUID id, @Valid @RequestBody UpdateCartItemRequestDto itemRequest) {
        Order updatedCart = orderService.updateCartItem(
                getAuthenticatedUserId(),
                id,
                itemRequest.getQuantity()
        );
        return ResponseEntity.ok(orderMapper.toOrderDto(updatedCart));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable UUID id) {
        orderService.deleteCartItem(getAuthenticatedUserId(), id);
        return ResponseEntity.noContent().build();
    }
}
