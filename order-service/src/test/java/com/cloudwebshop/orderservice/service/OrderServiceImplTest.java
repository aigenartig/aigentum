package com.cloudwebshop.orderservice.service;

import com.cloudwebshop.orderservice.model.Order;
import com.cloudwebshop.orderservice.model.OrderStatus;
import com.cloudwebshop.orderservice.repository.OrderItemRepository;
import com.cloudwebshop.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    // The mapper is no longer part of the service, so we remove the mock for it.
    // @Mock
    // private OrderMapper orderMapper;

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
        // When save is called for a new cart, return our cart object
        when(orderRepository.save(any(Order.class))).thenReturn(cart);

        Order result = orderService.getCart(userId);

        assertNotNull(result);
        assertEquals(cart.getId(), result.getId());
        verify(orderRepository).findFirstByUserIdAndStatus(userId, OrderStatus.CART);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrderFromCart_withEmptyCart_throwsException() {
        cart.setItems(Collections.emptyList());
        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.of(cart));

        assertThrows(IllegalStateException.class, () -> {
            orderService.createOrderFromCart(userId);
        });

        verify(orderRepository, never()).save(any(Order.class));
    }
}
