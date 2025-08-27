package com.cloudwebshop.userservice.mapper;

import com.cloudwebshop.userservice.dto.UserDto;
import com.cloudwebshop.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);
}
