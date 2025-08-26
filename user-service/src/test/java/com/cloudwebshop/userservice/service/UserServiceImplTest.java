package com.cloudwebshop.userservice.service;

import com.cloudwebshop.userservice.model.User;
import com.cloudwebshop.userservice.model.UserStatus;
import com.cloudwebshop.userservice.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void getUserProfile_whenUserExists_returnsUser() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("test1@example.com");
        user.setPasswordHash("password");
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        // When
        User foundUser = userService.getUserProfile(userId);

        // Then
        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
    }

    @Test
    void getUserProfile_whenUserDoesNotExist_throwsException() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserProfile(nonExistentId);
        });
    }

    @Test
    void deleteUserAccount_setsStatusToDeleted() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("test2@example.com");
        user.setPasswordHash("password");
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        // When
        userService.deleteUserAccount(userId);

        // Then
        User deletedUser = userRepository.findById(userId).orElseThrow();
        assertEquals(UserStatus.DELETED, deletedUser.getStatus());
    }

    @Test
    void updateUserProfile_whenUserExists_savesAndReturnsUser() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("test3@example.com");
        user.setPasswordHash("password");
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        user.setEmail("new.email@example.com");

        // When
        User result = userService.updateUserProfile(user);

        // Then
        assertNotNull(result);
        assertEquals("new.email@example.com", result.getEmail());
    }

    @Test
    void updateUserProfile_whenUserDoesNotExist_throwsException() {
        // Given
        User nonExistentUser = new User();
        nonExistentUser.setId(UUID.randomUUID());
        nonExistentUser.setEmail("nonexistent@example.com");

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUserProfile(nonExistentUser);
        });
    }
}
