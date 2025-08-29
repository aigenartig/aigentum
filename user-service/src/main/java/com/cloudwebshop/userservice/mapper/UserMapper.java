package com.cloudwebshop.userservice.mapper;

import com.cloudwebshop.userservice.dto.UserDto;
import com.cloudwebshop.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);

    @Mapping(target = "passwordHash", ignore = true)
    User toUser(UserDto userDto);
}
