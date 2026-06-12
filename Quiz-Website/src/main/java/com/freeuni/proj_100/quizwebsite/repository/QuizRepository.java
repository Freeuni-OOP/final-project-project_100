package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for Quiz entity.
 * Provides basic CRUD operations and custom queries for quizzes.
 */
public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    /**
     * Finds all quizzes created by a specific user.
     * @param creatorId the ID of the quiz creator.
     * @return list of quizzes created by that user.
     */
    List<Quiz> findByCreatorId(int creatorId);
}