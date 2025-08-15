package com.cloudwebshop.userservice.controller;

import com.cloudwebshop.userservice.dto.UpdateUserRequestDto;
import com.cloudwebshop.userservice.dto.UserDto;
import com.cloudwebshop.userservice.mapper.UserMapper;
import com.cloudwebshop.userservice.model.User;
import com.cloudwebshop.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        User userEntity = userService.getUserProfile(userId);
        return ResponseEntity.ok(userMapper.toUserDto(userEntity));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID userId, @Valid @RequestBody UpdateUserRequestDto requestDto) {
        User currentUser = userService.getUserProfile(userId);

        // Manually update fields from DTO if they are not null
        if (requestDto.getFirstName() != null) {
            currentUser.setFirstName(requestDto.getFirstName());
        }
        if (requestDto.getLastName() != null) {
            currentUser.setLastName(requestDto.getLastName());
        }
        if (requestDto.getEmail() != null) {
            currentUser.setEmail(requestDto.getEmail());
        }
        if (requestDto.getPhone() != null) {
            currentUser.setPhone(requestDto.getPhone());
        }

        User updatedUser = userService.updateUserProfile(currentUser);
        return ResponseEntity.ok(userMapper.toUserDto(updatedUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUserAccount(userId);
        return ResponseEntity.noContent().build();
    }
}
