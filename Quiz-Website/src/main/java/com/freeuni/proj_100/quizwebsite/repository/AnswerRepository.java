package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository responsible for retrieving answer choices.
 */
public interface AnswerRepository extends JpaRepository<AnswerEntity, Integer> {

    /**
     * Returns every answer option for a question.
     *
     * The answers are ordered by slot number so they
     * appear consistently in the UI.
     *
     * @param questionId question identifier
     * @return list of answer entities
     */
    @Query(value = """
            SELECT *
            FROM answers
            WHERE question_id = :questionId
            ORDER BY slot_num ASC, id ASC
            """, nativeQuery = true)
    List<AnswerEntity> findAllForQuestion(@Param("questionId") int questionId);

    /**
     * Returns only the correct answers for a question.
     *
     * Supports both single-answer and multiple-answer questions.
     *
     * @param questionId question identifier
     * @return list containing the correct answer entities
     */
    @Query(value = """
            SELECT *
            FROM answers
            WHERE question_id = :questionId
              AND is_correct = TRUE
            ORDER BY slot_num ASC, id ASC
            """, nativeQuery = true)
    List<AnswerEntity> findCorrectForQuestion(@Param("questionId") int questionId);
}