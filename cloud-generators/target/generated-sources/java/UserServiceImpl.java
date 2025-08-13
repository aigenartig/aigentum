package com.cloudwebshop.user.service;

import com.cloudwebshop.user.repository.UserRepository;
import com.cloudwebshop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Other imports will be needed for method implementations
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserProfile(UUID userId) {
        // TODO: Implement method logic
        return null;
    }
    @Override
    public User updateUserProfile(User user) {
        // TODO: Implement method logic
        return null;
    }
    @Override
    public void deleteUserAccount(UUID userId) {
        // TODO: Implement method logic
    }
}
