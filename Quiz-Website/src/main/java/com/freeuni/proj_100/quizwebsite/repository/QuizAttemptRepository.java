package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for QuizAttempt entity.
 * Provides operations to query user histories and paginated quiz leaderboards.
 */
@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    /**
     * Retrieves the history of non-practice quiz attempts for a specific user across all quizzes.
     */
    List<QuizAttempt> findByUserIdAndIsPracticeFalseOrderByTakenAtDesc(Long userId);

    /**
     * Highest performers of all time.
     * This serves as the primary leaderboard query.
     */
<<<<<<< Updated upstream
    List<QuizAttempt> findByQuizIdAndIsPracticeFalseOrderByScoreDescTimeTakenSecDesc(Long quizId, Pageable pageable);


    /**
     * Deletes the quiz attempt by id
     */
    void deleteByQuizId(Long quizId);
=======
    @Query("SELECT a FROM QuizAttempt a WHERE a.quizId = :quizId AND a.isPractice = false ORDER BY a.score DESC, a.timeTakenSec ASC")
    List<QuizAttempt> getTopPerformersAllTime(Long quizId, Pageable pageable);

    /**
     * Top performers in the last day.
     */
    @Query("SELECT a FROM QuizAttempt a WHERE a.quizId = :quizId AND a.isPractice = false AND a.takenAt >= :yesterday ORDER BY a.score DESC, a.timeTakenSec ASC")
    List<QuizAttempt> getTopPerformersLastDay(Long quizId, LocalDateTime yesterday, Pageable pageable);

    /**
     * Performance of recent test takers (Ranked sequentially by when they took it).
     */
    @Query("SELECT a FROM QuizAttempt a WHERE a.quizId = :quizId AND a.isPractice = false ORDER BY a.takenAt DESC")
    List<QuizAttempt> getRecentTestTakers(Long quizId, Pageable pageable);

    /**
     * User's past performance on this specific quiz.
     */
    @Query("SELECT a FROM QuizAttempt a WHERE a.quizId = :quizId AND a.user.username = :username AND a.isPractice = false ORDER BY a.takenAt DESC")
    List<QuizAttempt> getUserPastPerformance(Long quizId, String username);
>>>>>>> Stashed changes
}