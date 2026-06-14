package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import com.freeuni.proj_100.quizwebsite.model.UserAchievement;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import com.freeuni.proj_100.quizwebsite.repository.QuizAttemptRepository;
import com.freeuni.proj_100.quizwebsite.repository.UserAchievementRepository;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST Controller for managing user profiles.
 * Provides endpoints to retrieve a user's basic info, quiz history, and achievements.
 */
@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    private final UserRepository userRepository;
    private final QuizAttemptRepository attemptRepository;
    private final UserAchievementRepository achievementRepository;

    /**
     * Constructs the ProfileController with required repositories.
     *
     * @param userRepository        Repository for user data.
     * @param attemptRepository     Repository for quiz attempt history.
     * @param achievementRepository Repository for user achievements.
     */
    public ProfileController(UserRepository userRepository,
                             QuizAttemptRepository attemptRepository,
                             UserAchievementRepository achievementRepository) {
        this.userRepository = userRepository;
        this.attemptRepository = attemptRepository;
        this.achievementRepository = achievementRepository;
    }

    /**
     * Retrieves the profile data for a specific user.
     * Determines if the currently authenticated user is viewing their own profile or someone else's.
     *
     * @param username       The username of the profile being requested.
     * @param authentication The current authentication context.
     * @return A ResponseEntity containing a map of profile data, or 404 if not found.
     */
    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable String username, Authentication authentication) {
        Optional<User> targetUser = userRepository.findByUsername(username);
        if (targetUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = targetUser.get();
        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("isAdmin", user.isAdmin());
        response.put("joinedAt", user.getCreatedAt());

        String loggedInUsername = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                loggedInUsername = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            } else if (principal instanceof User) {
                loggedInUsername = ((User) principal).getUsername();
            } else {
                loggedInUsername = principal.toString();
            }
        }

        if (user.getUsername().equalsIgnoreCase(loggedInUsername)) {
            response.put("relation", "self");
        } else {
            response.put("relation", "viewer");
        }

        List<QuizAttempt> recentAttempts = attemptRepository.getUserHistory((long) user.getId());
        response.put("recentAttempts", recentAttempts);

        List<UserAchievement> achievementEntities = achievementRepository.findByUserId((long) user.getId());
        List<String> achievementStrings = achievementEntities.stream()
                .map(UserAchievement::getAchievementType)
                .collect(Collectors.toList());

        response.put("achievements", achievementStrings);

        return ResponseEntity.ok(response);
    }
}