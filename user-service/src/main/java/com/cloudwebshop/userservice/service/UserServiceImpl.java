package com.cloudwebshop.userservice.service;

import com.cloudwebshop.userservice.model.User;
import com.cloudwebshop.userservice.model.UserStatus;
import com.cloudwebshop.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User getUserProfile(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    @Override
    @Transactional
    public User updateUserProfile(User user) {
        // The user object is passed in already updated from the controller.
        // The service's responsibility is simply to persist it.
        // We ensure the user exists before trying to save.
        if (!userRepository.existsById(user.getId())) {
            throw new EntityNotFoundException("Cannot update non-existent user with id: " + user.getId());
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserAccount(UUID userId) {
        User user = getUserProfile(userId);
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        // In a real app, you might want to add more validation or logic here
        // For example, checking if an email is already in use
        user.setStatus(UserStatus.ACTIVE);
        user.setPasswordHash("dummy_password_hash");
        return userRepository.save(user);
    }
}
