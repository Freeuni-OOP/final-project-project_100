package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for UserAchievement entity.
 * Provides standard CRUD operations and custom queries for user achievements.
 */
@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {

    /**
     * Finds all achievements earned by a specific user.
     *
     * @param userId The unique ID of the user.
     * @return A list of achievements associated with the user.
     */
    List<UserAchievement> findByUserId(Long userId);
}