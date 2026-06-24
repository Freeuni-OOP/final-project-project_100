package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.model.Quiz;
import com.freeuni.proj_100.quizwebsite.repository.QuizRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for quiz-related business logic.
 * Sits between the controller and repository.
 */
@Service
public class QuizService {

    private final QuizRepository quizRepository;

    /**
     * Spring automatically injects QuizRepository here.
     */
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    /**
     * Gets a quiz by its ID.
     */
    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    /**
     * Gets all quizzes.
     */
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    /**
     * Gets all quizzes created by a specific user.
     */
    public List<Quiz> getQuizzesByCreator(Long creatorId) {
        return quizRepository.findByCreatorId(creatorId);
    }
}