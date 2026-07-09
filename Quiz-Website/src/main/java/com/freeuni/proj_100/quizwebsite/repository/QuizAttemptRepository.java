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
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Integer> {

    /**
     * Retrieves the history of non-practice quiz attempts for a specific user, ordered by most recent.
     */
    List<QuizAttempt> findByUserIdAndIsPracticeFalseOrderByTakenAtDesc(Integer userId);

    /**
     * Retrieves the leaderboard for a specific quiz based on score (descending) and time taken (ascending).
     * Now accepts a Pageable object to prevent memory overload.
     */
    List<QuizAttempt> findByQuizIdAndIsPracticeFalseOrderByScoreDescTimeTakenSecDesc(Integer quizId, Pageable pageable);


    /**
     * Deletes the quiz attempt by id
     */
    void deleteByQuizId(Integer quizId);
}