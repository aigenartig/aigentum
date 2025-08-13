package com.cloudwebshop.order.service;

// Import statements will need to be added here.
// A more advanced generator would collect all unique types and generate imports.
import java.util.UUID;
import java.util.List;
import com.cloudwebshop.userservice.model.User; // Example, needs to be dynamic
import com.cloudwebshop.orderservice.model.Order; // Example, needs to be dynamic

public interface OrderService {

    Order getCart(UUID userId);
    Order addItemToCart(UUID userId, UUID productId, Integer quantity);
    Order updateCartItem(UUID userId, UUID itemId, Integer quantity);
    void deleteCartItem(UUID userId, UUID itemId);
    List<Order<> getOrdersForUser(UUID userId);
    Order createOrderFromCart(UUID userId);
}
