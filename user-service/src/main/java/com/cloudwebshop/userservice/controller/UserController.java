package com.cloudwebshop.userservice.controller;

import com.cloudwebshop.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile() {
        // For now, assuming a hardcoded user or principal would be used
        // This is a placeholder
        String userId = "temp-user-id";
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<Object> updateProfile(@RequestBody Object profileDetails) {
        // Placeholder
        String userId = "temp-user-id";
        return ResponseEntity.ok(userService.updateUserProfile(userId, profileDetails));
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount() {
        // Placeholder
        String userId = "temp-user-id";
        userService.deleteUserAccount(userId);
        return ResponseEntity.noContent().build();
    }
}
