package com.cloudwebshop.orderservice.service;

import com.cloudwebshop.orderservice.dto.AddItemRequestDto;
import com.cloudwebshop.orderservice.dto.OrderDto;
import com.cloudwebshop.orderservice.dto.UpdateCartItemRequestDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    // Cart methods
    OrderDto getCart(UUID userId);
    OrderDto addItemToCart(UUID userId, AddItemRequestDto itemRequest);
    OrderDto updateCartItem(UUID userId, UUID itemId, UpdateCartItemRequestDto itemRequest);
    void deleteCartItem(UUID userId, UUID itemId);

    // Order methods
    List<OrderDto> getOrdersForUser(UUID userId);
    OrderDto createOrderFromCart(UUID userId);
    OrderDto getOrderById(UUID orderId);
    Object updateOrderStatus(String orderId, String status);
}
