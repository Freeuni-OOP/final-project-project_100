package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for QuizAttempt entity.
 * Provides operations to query user histories and paginated quiz leaderboards.
 */
@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    /**
     * Retrieves the history of non-practice quiz attempts for a specific user, ordered by most recent.
     */
    @Query("SELECT a FROM QuizAttempt a WHERE a.user.id = :userId AND a.isPractice = false ORDER BY a.takenAt DESC")
    List<QuizAttempt> getUserHistory(Long userId);

    /**
     * Retrieves the leaderboard for a specific quiz based on score (descending) and time taken (ascending).
     * Now accepts a Pageable object to prevent memory overload.
     */
    @Query("SELECT a FROM QuizAttempt a WHERE a.quizId = :quizId AND a.isPractice = false ORDER BY a.score DESC, a.timeTakenSec ASC")
    List<QuizAttempt> getLeaderboard(Long quizId, Pageable pageable);
}