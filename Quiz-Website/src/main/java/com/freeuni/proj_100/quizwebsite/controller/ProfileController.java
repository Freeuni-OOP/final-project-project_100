package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.ProfileResponseDTO;
import com.freeuni.proj_100.quizwebsite.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing user profiles.
 * Delegates all logic to ProfileService and returns isolated DTOs.
 */
@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "http://localhost:5173")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Retrieves the profile data for a specific user.
     */
    @GetMapping("/{username}")
    public ResponseEntity<ProfileResponseDTO> getProfile(@PathVariable String username, Authentication authentication) {
        return profileService.getProfileData(username, authentication)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}