package com.freeuni.proj_100.quizwebsite.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/* JUST A STUB */
@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    void deleteByQuizId(Long quizId);
}