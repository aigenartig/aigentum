package com.cloudwebshop.orderservice.service;

import com.cloudwebshop.orderservice.dto.AddItemRequestDto;
import com.cloudwebshop.orderservice.dto.OrderDto;
import com.cloudwebshop.orderservice.dto.UpdateCartItemRequestDto;
import com.cloudwebshop.orderservice.mapper.OrderMapper;
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
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public OrderDto getCart(UUID userId) {
        Order cart = findOrCreateCart(userId);
        return orderMapper.toOrderDto(cart);
    }

    @Override
    @Transactional
    public OrderDto addItemToCart(UUID userId, AddItemRequestDto itemRequest) {
        Order cart = findOrCreateCart(userId);

        // Check if item already exists
        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemRequest.getProductId()))
                .findFirst()
                .ifPresentOrElse(
                        // If it exists, update quantity
                        item -> item.setQuantity(item.getQuantity() + itemRequest.getQuantity()),
                        // If not, create a new item
                        () -> {
                            OrderItem newItem = new OrderItem();
                            newItem.setProductId(itemRequest.getProductId());
                            newItem.setQuantity(itemRequest.getQuantity());
                            // In a real scenario, we'd fetch the price from the Product Service
                            newItem.setUnitPrice(new BigDecimal("99.99")); // Placeholder price
                            newItem.setOrder(cart);
                            cart.getItems().add(newItem);
                        }
                );

        recalculateCartTotals(cart);
        Order savedCart = orderRepository.save(cart);
        return orderMapper.toOrderDto(savedCart);
    }

    @Override
    @Transactional
    public OrderDto updateCartItem(UUID userId, UUID itemId, UpdateCartItemRequestDto itemRequest) {
        Order cart = findOrCreateCart(userId);
        OrderItem itemToUpdate = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item not found in cart"));

        itemToUpdate.setQuantity(itemRequest.getQuantity());
        recalculateCartTotals(cart);
        Order savedCart = orderRepository.save(cart);
        return orderMapper.toOrderDto(savedCart);
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

    // --- Private Helper Methods ---

    private Order findOrCreateCart(UUID userId) {
        return orderRepository.findFirstByUserIdAndStatus(userId, OrderStatus.CART)
                .orElseGet(() -> createNewCart(userId));
    }

    private Order createNewCart(UUID userId) {
        Order newCart = new Order();
        newCart.setUserId(userId);
        newCart.setStatus(OrderStatus.CART);
        newCart.setCurrency("USD"); // Default currency
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


    // --- Placeholder Methods for Order Logic ---

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersForUser(UUID userId) {
        List<Order> orders = orderRepository.findAllByUserIdAndStatusNot(userId, OrderStatus.CART);
        return orderMapper.toOrderDtoList(orders);
    }

    @Override
    @Transactional
    public OrderDto createOrderFromCart(UUID userId) {
        Order cart = findOrCreateCart(userId);

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order from an empty cart.");
        }

        cart.setStatus(OrderStatus.PENDING);
        cart.setOrderNumber(generateOrderNumber());

        // The totals are already calculated every time the cart is modified.
        // A final price check could be added here if needed.

        Order placedOrder = orderRepository.save(cart);

        // After placing the order, the user should get a new, empty cart.
        // The old cart is now an order, so we don't need to create a new one explicitly
        // until the next time findOrCreateCart is called.

        return orderMapper.toOrderDto(placedOrder);
    }

    private String generateOrderNumber() {
        // Simple order number generation strategy
        return "ORD-" + System.currentTimeMillis() + "-" + (UUID.randomUUID().toString().substring(0, 4));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toOrderDto(order);
    }

    @Override
    public Object updateOrderStatus(String orderId, String status) {
        // Placeholder
        return null;
    }
}
