package com.cloudwebshop.orderservice.service;

import com.cloudwebshop.orderservice.dto.AddItemRequestDto;
import com.cloudwebshop.orderservice.dto.UpdateCartItemRequestDto;
import com.cloudwebshop.orderservice.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    // Cart methods
    Order getCart(UUID userId);
    Order addItemToCart(UUID userId, AddItemRequestDto itemRequest);
    Order updateCartItem(UUID userId, UUID itemId, UpdateCartItemRequestDto itemRequest);
    void deleteCartItem(UUID userId, UUID itemId);

    // Order methods
    List<Order> getOrdersForUser(UUID userId);
    Order createOrderFromCart(UUID userId);
    Order getOrderById(UUID orderId);
    Order updateOrderStatus(UUID orderId, String status);
}
