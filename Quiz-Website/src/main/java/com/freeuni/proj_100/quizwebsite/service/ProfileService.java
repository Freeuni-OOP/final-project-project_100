package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.ProfileResponseDTO;
import com.freeuni.proj_100.quizwebsite.dto.QuizAttemptDTO;
import com.freeuni.proj_100.quizwebsite.model.Friendship;
import com.freeuni.proj_100.quizwebsite.model.FriendshipStatus;
import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.model.UserAchievement;
import com.freeuni.proj_100.quizwebsite.repository.FriendshipRepository;
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
    private final FriendshipRepository friendshipRepository;

    public ProfileService(UserRepository userRepository, QuizAttemptRepository attemptRepository, UserAchievementRepository achievementRepository, FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.attemptRepository = attemptRepository;
        this.achievementRepository = achievementRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public Optional<ProfileResponseDTO> getProfileData(String targetUsername, Authentication auth) {
        return userRepository.findByUsername(targetUsername).map(user -> {

            String relation = determineRelation(user, auth);
            List<QuizAttemptDTO> attempts = fetchRecentAttempts(user.getId());
            List<String> achievements = fetchAchievements(user.getId());

            return new ProfileResponseDTO(
                    user.getId(),
                    user.getUsername(), user.getEmail(), user.isAdmin(),
                    user.getCreatedAt(), relation, attempts, achievements
            );
        });
    }

    private String determineRelation(User targetUser, Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            return "NONE";
        }

        String currentUsername = auth.getName();
        if (targetUser.getUsername().equalsIgnoreCase(currentUsername)) {
            return "SELF";
        }

        Optional<User> currentUserOpt = userRepository.findByUsername(currentUsername);
        if (currentUserOpt.isEmpty()) {
            return "NONE";
        }
        User currentUser = currentUserOpt.get();

        Optional<Friendship> friendshipOpt = friendshipRepository.findFriendshipBetween(currentUser.getId(), targetUser.getId());

        if (friendshipOpt.isEmpty()) {
            return "NONE";
        }

        Friendship friendship = friendshipOpt.get();

        if (friendship.getStatus() == FriendshipStatus.ACCEPTED) {
            return "FRIEND";
        } else if (friendship.getStatus() == FriendshipStatus.PENDING) {
            if (friendship.getUserId().equals(currentUser.getId())) {
                return "PENDING_OUTGOING";
            } else {
                return "PENDING_INCOMING";
            }
        }

        return "NONE";
    }

    private List<QuizAttemptDTO> fetchRecentAttempts(Integer userId) {
        return attemptRepository.findByUserIdAndIsPracticeFalseOrderByTakenAtDesc(userId).stream()
                // Safely uses the updated QuizAttemptDTO constructor we created earlier
                .map(QuizAttemptDTO::new)
                .toList();
    }

    private List<String> fetchAchievements(Integer userId) {
        return achievementRepository.findByUser_Id(userId).stream()
                .map(UserAchievement::getAchievementType)
                .toList();
    }
}