package com.cloudwebshop.userservice.service;

import com.cloudwebshop.userservice.model.User;
import com.cloudwebshop.userservice.model.UserStatus;
import com.cloudwebshop.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceDataIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndRetrieveUser() {
        // Given
        UUID userId = UUID.randomUUID();
        User newUser = new User();
        newUser.setId(userId);
        newUser.setEmail("integration.test@example.com");
        newUser.setPasswordHash("password");
        newUser.setStatus(UserStatus.ACTIVE);
        userRepository.save(newUser);

        // When
        User foundUser = userService.getUserProfile(userId);

        // Then
        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        assertEquals("integration.test@example.com", foundUser.getEmail());
    }
}
