package com.cloudwebshop.userservice.service;

import com.cloudwebshop.userservice.model.User;

import java.util.UUID;

public interface UserService {

    User getUserProfile(UUID userId);

    User updateUserProfile(User user);

    void deleteUserAccount(UUID userId);

    User createUser(User user);
}
