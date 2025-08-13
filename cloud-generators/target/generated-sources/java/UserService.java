package com.cloudwebshop.user.service;

// Import statements will need to be added here.
// A more advanced generator would collect all unique types and generate imports.
import java.util.UUID;
import java.util.List;
import com.cloudwebshop.userservice.model.User; // Example, needs to be dynamic
import com.cloudwebshop.orderservice.model.Order; // Example, needs to be dynamic

public interface UserService {

    User getUserProfile(UUID userId);
    User updateUserProfile(User user);
    void deleteUserAccount(UUID userId);
}
