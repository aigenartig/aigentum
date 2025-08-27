package com.cloudwebshop.userservice.dto;

import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
}
