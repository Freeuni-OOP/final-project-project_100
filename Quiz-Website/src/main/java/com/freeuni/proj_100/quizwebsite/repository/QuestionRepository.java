package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository responsible for accessing question records.
 */
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {

    /**
     * Returns all questions belonging to a specific quiz.
     *
     * Questions are ordered by sequence number so that the quiz
     * appears in the intended order.
     *
     * @param quizId ID of the quiz
     * @return ordered list of question entities
     */
    @Query(value = """
            SELECT *
            FROM questions
            WHERE quiz_id = :quizId
            ORDER BY sequence_num ASC, id ASC
            """, nativeQuery = true)
    List<QuestionEntity> findAllForQuiz(@Param("quizId") int quizId);
}