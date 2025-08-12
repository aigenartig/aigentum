package com.cloudwebshop.userservice.service;

import com.cloudwebshop.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Object getUserProfile(String userId) {
        // Placeholder implementation
        return null;
    }

    @Override
    public Object updateUserProfile(String userId, Object profileDetails) {
        // Placeholder implementation
        return null;
    }

    @Override
    public void deleteUserAccount(String userId) {
        // Placeholder implementation
    }
}
