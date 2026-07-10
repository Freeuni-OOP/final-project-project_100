package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for reading and writing answer rows.
 */
public interface AnswerRepository extends JpaRepository<AnswerEntity, Integer> {
    /**
     * Finds all answers for a question in display order.
     * Used mainly for loading multiple-choice options.
     *
     * @param questionId parent question identifier.
     * @return ordered answers for the question.
     */
    List<AnswerEntity> findByQuestionIdOrderBySlotNumAscIdAsc(int questionId);

    /**
     * Finds only correct answers for a question in display order.
     *
     * @param questionId parent question identifier.
     * @return ordered correct answers for the question.
     */
    List<AnswerEntity> findByQuestionIdAndCorrectTrueOrderBySlotNumAscIdAsc(int questionId);
}