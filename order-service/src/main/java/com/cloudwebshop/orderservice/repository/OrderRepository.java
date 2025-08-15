package com.cloudwebshop.orderservice.repository;

import com.cloudwebshop.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudwebshop.orderservice.model.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findFirstByUserIdAndStatus(UUID userId, OrderStatus status);

    List<Order> findAllByUserIdAndStatusNot(UUID userId, OrderStatus status);
}
