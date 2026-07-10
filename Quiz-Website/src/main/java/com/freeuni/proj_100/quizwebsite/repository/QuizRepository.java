package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import com.freeuni.proj_100.quizwebsite.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
    List<Quiz> findByCreatorId(Integer creatorId);

    @Query("SELECT DISTINCT q FROM Quiz q LEFT JOIN FETCH q.questions WHERE q.id = :id")
    Optional<Quiz> findByIdWithQuestions(@Param("id") Integer id);

    @Query("SELECT DISTINCT q FROM QuestionEntity q LEFT JOIN FETCH q.answers WHERE q.quiz.id = :quizId")
    List<QuestionEntity> findQuestionsWithAnswers(@Param("quizId") Integer quizId);
}