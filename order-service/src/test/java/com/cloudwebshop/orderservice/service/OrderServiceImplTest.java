package com.cloudwebshop.orderservice.service;

import com.cloudwebshop.orderservice.model.Order;
import com.cloudwebshop.orderservice.model.OrderItem;
import com.cloudwebshop.orderservice.model.OrderStatus;
import com.cloudwebshop.orderservice.repository.OrderItemRepository;
import com.cloudwebshop.orderservice.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private UUID userId;
    private Order cart;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        cart = new Order();
        cart.setId(UUID.randomUUID());
        cart.setUserId(userId);
        cart.setStatus(OrderStatus.CART);
        cart.setItems(new ArrayList<>()); // Ensure items list is not null
    }

    @Test
    void getCart_whenCartExists_returnsEntity() {
        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.of(cart));
        Order result = orderService.getCart(userId);
        assertNotNull(result);
        assertEquals(cart.getId(), result.getId());
        verify(orderRepository).findFirstByUserIdAndStatus(userId, OrderStatus.CART);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getCart_whenCartDoesNotExist_createsAndReturnsEntity() {
        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.empty());
        when(orderRepository.save(any(Order.class))).thenReturn(cart);
        Order result = orderService.getCart(userId);
        assertNotNull(result);
        assertEquals(cart.getId(), result.getId());
        verify(orderRepository).findFirstByUserIdAndStatus(userId, OrderStatus.CART);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void addItemToCart_addNewItem_createsNewOrderItem() {
        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(cart);

        UUID productId = UUID.randomUUID();
        int initialSize = cart.getItems().size();

        orderService.addItemToCart(userId, productId, 2);

        assertEquals(initialSize + 1, cart.getItems().size());
        OrderItem newItem = cart.getItems().get(initialSize);
        assertEquals(productId, newItem.getProductId());
        assertEquals(2, newItem.getQuantity());
        verify(orderRepository).save(cart);
    }

    @Test
    void createOrderFromCart_withEmptyCart_throwsException() {
        cart.setItems(Collections.emptyList());
        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.of(cart));
        assertThrows(IllegalStateException.class, () -> orderService.createOrderFromCart(userId));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void updateCartItem_whenItemExists_updatesQuantity() {
        UUID itemId = UUID.randomUUID();
        OrderItem item = new OrderItem();
        item.setId(itemId);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("10.00"));
        cart.getItems().add(item);

        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.updateCartItem(userId, itemId, 5);

        assertEquals(1, result.getItems().size());
        assertEquals(5, result.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("50.00"), result.getTotalAmount());
        verify(orderRepository).save(cart);
    }

    @Test
    void updateCartItem_whenItemDoesNotExist_throwsException() {
        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.of(cart));

        UUID nonExistentItemId = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.updateCartItem(userId, nonExistentItemId, 5);
        });
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void deleteCartItem_whenItemExists_removesItem() {
        UUID itemId = UUID.randomUUID();
        OrderItem item = new OrderItem();
        item.setId(itemId);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("10.00"));
        cart.getItems().add(item);

        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.of(cart));

        orderService.deleteCartItem(userId, itemId);

        assertTrue(cart.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, cart.getTotalAmount());
        verify(orderRepository).save(cart);
    }

    @Test
    void deleteCartItem_whenItemDoesNotExist_throwsException() {
        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.of(cart));
        UUID nonExistentItemId = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.deleteCartItem(userId, nonExistentItemId);
        });
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getOrdersForUser_returnsOnlyNonCartOrders() {
        Order deliveredOrder = new Order();
        deliveredOrder.setStatus(OrderStatus.DELIVERED);
        when(orderRepository.findAllByUserIdAndStatusNot(userId, OrderStatus.CART)).thenReturn(Collections.singletonList(deliveredOrder));

        var result = orderService.getOrdersForUser(userId);

        assertEquals(1, result.size());
        assertEquals(OrderStatus.DELIVERED, result.get(0).getStatus());
        verify(orderRepository).findAllByUserIdAndStatusNot(userId, OrderStatus.CART);
    }
}
