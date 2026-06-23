package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.ProfileResponseDTO;
import com.freeuni.proj_100.quizwebsite.dto.QuizAttemptDTO;
import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.model.UserAchievement;
import com.freeuni.proj_100.quizwebsite.repository.QuizAttemptRepository;
import com.freeuni.proj_100.quizwebsite.repository.UserAchievementRepository;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final QuizAttemptRepository attemptRepository;
    private final UserAchievementRepository achievementRepository;

    public ProfileService(UserRepository userRepository, QuizAttemptRepository attemptRepository, UserAchievementRepository achievementRepository) {
        this.userRepository = userRepository;
        this.attemptRepository = attemptRepository;
        this.achievementRepository = achievementRepository;
    }


    public Optional<ProfileResponseDTO> getProfileData(String targetUsername, Authentication auth) {
        return userRepository.findByUsername(targetUsername).map(user -> {

            String relation = determineRelation(user.getUsername(), auth);
            List<QuizAttemptDTO> attempts = fetchRecentAttempts(user.getId());
            List<String> achievements = fetchAchievements(user.getId());

            return new ProfileResponseDTO(
                    user.getUsername(), user.getEmail(), user.isAdmin(),
                    user.getCreatedAt(), relation, attempts, achievements
            );
        });
    }

    // PRIVATE HELPER METHODS (Hiding the messy mapping logic down here)

    private String determineRelation(String targetUsername, Authentication auth) {
        // simple security check using .getName()
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            return "viewer";
        }
        return targetUsername.equalsIgnoreCase(auth.getName()) ? "self" : "viewer";
    }

    private List<QuizAttemptDTO> fetchRecentAttempts(Long userId) {
        return attemptRepository.getUserHistory(userId).stream()
                .map(a -> new QuizAttemptDTO(a.getId(), a.getUser().getUsername(), a.getQuizId(), a.getScore(), a.getTimeTakenSec(), a.getTakenAt()))
                .toList();
    }

    private List<String> fetchAchievements(Long userId) {
        return achievementRepository.findByUser_Id(userId).stream()
                .map(UserAchievement::getAchievementType)
                .toList();
    }
}