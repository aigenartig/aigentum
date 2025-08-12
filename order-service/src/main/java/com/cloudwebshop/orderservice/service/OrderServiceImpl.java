package com.cloudwebshop.orderservice.service;

import com.cloudwebshop.orderservice.model.Order;
import com.cloudwebshop.orderservice.model.OrderItem;
import com.cloudwebshop.orderservice.model.OrderStatus;
import com.cloudwebshop.orderservice.repository.OrderItemRepository;
import com.cloudwebshop.orderservice.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional(readOnly = true)
    public Order getCart(UUID userId) {
        return findOrCreateCart(userId);
    }

    @Override
    @Transactional
    public Order addItemToCart(UUID userId, UUID productId, Integer quantity) {
        Order cart = findOrCreateCart(userId);
        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> {
                            OrderItem newItem = new OrderItem();
                            newItem.setProductId(productId);
                            newItem.setQuantity(quantity);
                            newItem.setUnitPrice(new BigDecimal("99.99")); // Placeholder price
                            newItem.setOrder(cart);
                            cart.getItems().add(newItem);
                        }
                );
        recalculateCartTotals(cart);
        return orderRepository.save(cart);
    }

    @Override
    @Transactional
    public Order updateCartItem(UUID userId, UUID itemId, Integer quantity) {
        Order cart = findOrCreateCart(userId);
        OrderItem itemToUpdate = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item not found in cart"));
        itemToUpdate.setQuantity(quantity);
        recalculateCartTotals(cart);
        return orderRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteCartItem(UUID userId, UUID itemId) {
        Order cart = findOrCreateCart(userId);
        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(itemId));
        if (!removed) {
            throw new EntityNotFoundException("Item not found in cart");
        }
        recalculateCartTotals(cart);
        orderRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersForUser(UUID userId) {
        return orderRepository.findAllByUserIdAndStatusNot(userId, OrderStatus.CART);
    }

    @Override
    @Transactional
    public Order createOrderFromCart(UUID userId) {
        Order cart = findOrCreateCart(userId);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order from an empty cart.");
        }
        cart.setStatus(OrderStatus.PENDING);
        cart.setOrderNumber(generateOrderNumber());
        return orderRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    @Override
    @Transactional
    public Order updateOrderStatus(UUID orderId, String status) {
        Order order = getOrderById(orderId);
        try {
            OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
            return orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

    private Order findOrCreateCart(UUID userId) {
        return orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)
                .orElseGet(() -> createNewCart(userId));
    }

    private Order createNewCart(UUID userId) {
        Order newCart = new Order();
        newCart.setUserId(userId);
        newCart.setStatus(OrderStatus.CART);
        newCart.setCurrency("USD");
        newCart.setItems(new ArrayList<>());
        newCart.setTotalAmount(BigDecimal.ZERO);
        return orderRepository.save(newCart);
    }

    private void recalculateCartTotals(Order cart) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : cart.getItems()) {
            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setTotalPrice(itemTotal);
            total = total.add(itemTotal);
        }
        cart.setTotalAmount(total);
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + (UUID.randomUUID().toString().substring(0, 4));
    }
}
