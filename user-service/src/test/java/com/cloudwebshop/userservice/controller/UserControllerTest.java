package com.cloudwebshop.userservice.controller;

import com.cloudwebshop.userservice.dto.UpdateUserRequestDto;
import com.cloudwebshop.userservice.dto.UserDto;
import com.cloudwebshop.userservice.mapper.UserMapper;
import com.cloudwebshop.userservice.model.User;
import com.cloudwebshop.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserDto userDto;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");

        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setEmail("test@example.com");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
    }

    @Test
    void getUserById_whenUserExists_returnsUserDto() throws Exception {
        when(userService.getUserProfile(userId)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void updateUser_whenUserExists_returnsUpdatedUserDto() throws Exception {
        UpdateUserRequestDto updateUserRequestDto = new UpdateUserRequestDto();
        updateUserRequestDto.setFirstName("Updated");
        updateUserRequestDto.setLastName("Name");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail("test@example.com");
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("Name");

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(userId);
        updatedUserDto.setEmail("test@example.com");
        updatedUserDto.setFirstName("Updated");
        updatedUserDto.setLastName("Name");

        when(userService.getUserProfile(userId)).thenReturn(user);
        when(userService.updateUserProfile(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toUserDto(updatedUser)).thenReturn(updatedUserDto);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Name"));
    }

    @Test
    void deleteUser_whenUserExists_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());
    }
}
