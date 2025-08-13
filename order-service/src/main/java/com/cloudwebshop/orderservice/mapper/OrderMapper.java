package com.cloudwebshop.orderservice.mapper;

import com.cloudwebshop.orderservice.dto.OrderDto;
import com.cloudwebshop.orderservice.dto.OrderItemDto;
import com.cloudwebshop.orderservice.model.Order;
import com.cloudwebshop.orderservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    // Order to OrderDto
    @Mapping(source = "status", target = "status") // Example of explicit mapping if needed, though not required here
    OrderDto toOrderDto(Order order);

    // OrderItem to OrderItemDto
    OrderItemDto toOrderItemDto(OrderItem orderItem);

    // List of Orders to List of OrderDtos
    List<OrderDto> toOrderDtoList(List<Order> orders);

    // Mapping from DTO back to Entity could be added here if needed,
    // but it's often handled more carefully (e.g., not mapping IDs).
}
