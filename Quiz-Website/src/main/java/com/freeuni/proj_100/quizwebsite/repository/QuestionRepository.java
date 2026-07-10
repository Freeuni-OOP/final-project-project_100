package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for reading and writing quiz question rows.
 */
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
    /**
     * Finds quiz questions in stable display order and fetches answers together.
     * The entity graph avoids the N+1 query problem while building runtime Question objects.
     *
     * @param quizId parent quiz identifier.
     * @return ordered questions with their answers loaded.
     */
    @EntityGraph(attributePaths = "answers")
    List<QuestionEntity> findByQuizIdOrderBySequenceNumAscIdAsc(int quizId);

    @Query("SELECT DISTINCT q FROM QuestionEntity q LEFT JOIN FETCH q.answers WHERE q.quiz.id = :quizId")
    List<QuestionEntity> findQuestionsWithAnswers(@Param("quizId") Integer quizId);
}