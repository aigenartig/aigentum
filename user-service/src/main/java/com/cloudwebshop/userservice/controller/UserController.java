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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    private UUID getAuthenticatedUserId() {
        // In a real app, this comes from the security context
        return UUID.fromString("11111111-1111-1111-1111-111111111111");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile() {
        User userEntity = userService.getUserProfile(getAuthenticatedUserId());
        return ResponseEntity.ok(userMapper.toUserDto(userEntity));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(@Valid @RequestBody UpdateUserRequestDto requestDto) {
        UUID userId = getAuthenticatedUserId();
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

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount() {
        userService.deleteUserAccount(getAuthenticatedUserId());
        return ResponseEntity.noContent().build();
    }
}
