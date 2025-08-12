package com.cloudwebshop.orderservice.service;

import com.cloudwebshop.orderservice.dto.OrderDto;
import com.cloudwebshop.orderservice.mapper.OrderMapper;
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

    @Mock
    private OrderMapper orderMapper;

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
    void getCart_whenCartExists_returnsDto() {
        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.of(cart));
        when(orderMapper.toOrderDto(cart)).thenReturn(new OrderDto());

        OrderDto result = orderService.getCart(userId);

        assertNotNull(result);
        verify(orderRepository).findFirstByUserIdAndStatus(userId, OrderStatus.CART);
        verify(orderMapper).toOrderDto(cart);
    }

    @Test
    void getCart_whenCartDoesNotExist_createsAndReturnsDto() {
        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.empty());
        when(orderRepository.save(any(Order.class))).thenReturn(cart);
        when(orderMapper.toOrderDto(cart)).thenReturn(new OrderDto());

        OrderDto result = orderService.getCart(userId);

        assertNotNull(result);
        verify(orderRepository).findFirstByUserIdAndStatus(userId, OrderStatus.CART);
        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).toOrderDto(cart);
    }

    @Test
    void createOrderFromCart_withEmptyCart_throwsException() {
        cart.setItems(java.util.Collections.emptyList());
        when(orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)).thenReturn(Optional.of(cart));

        assertThrows(IllegalStateException.class, () -> {
            orderService.createOrderFromCart(userId);
        });

        verify(orderRepository, never()).save(any(Order.class));
    }
}
