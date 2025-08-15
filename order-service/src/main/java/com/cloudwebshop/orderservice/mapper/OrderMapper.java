package com.cloudwebshop.orderservice.mapper;

import com.cloudwebshop.orderservice.dto.OrderDto;
import com.cloudwebshop.orderservice.dto.OrderItemDto;
import com.cloudwebshop.orderservice.model.Order;
import com.cloudwebshop.orderservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Order to OrderDto
    @Mapping(source = "items", target = "items")
    @Mapping(source = "status", target = "status")
    OrderDto toOrderDto(Order order);

    // OrderItem to OrderItemDto
    OrderItemDto toOrderItemDto(OrderItem orderItem);

    // List of Orders to List of OrderDtos
    List<OrderDto> toOrderDtoList(List<Order> orders);

    // Mapping from DTO back to Entity could be added here if needed,
    // but it's often handled more carefully (e.g., not mapping IDs).
}
