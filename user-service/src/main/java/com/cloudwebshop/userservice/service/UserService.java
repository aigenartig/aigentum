package com.cloudwebshop.userservice.service;

// This will be expanded with DTOs later
public interface UserService {
    // For now, we'll just define placeholder methods.
    // In a real implementation, these would take and return DTOs.

    /**
     * Gets the profile for a user.
     * @param userId The ID of the user.
     * @return A representation of the user's profile.
     */
    Object getUserProfile(String userId);

    /**
     * Updates a user's profile.
     * @param userId The ID of the user to update.
     * @param profileDetails The new details for the profile.
     * @return The updated profile.
     */
    Object updateUserProfile(String userId, Object profileDetails);

    /**
     * Deletes a user's account.
     * @param userId The ID of the user to delete.
     */
    void deleteUserAccount(String userId);
}
