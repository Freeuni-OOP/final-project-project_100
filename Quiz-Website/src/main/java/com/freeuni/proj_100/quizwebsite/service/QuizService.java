package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import com.freeuni.proj_100.quizwebsite.model.Quiz;
import com.freeuni.proj_100.quizwebsite.repository.AnswerRepository;
import com.freeuni.proj_100.quizwebsite.repository.QuestionRepository;
import com.freeuni.proj_100.quizwebsite.repository.QuizRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.freeuni.proj_100.quizwebsite.dto.QuizCreationDTO;
import com.freeuni.proj_100.quizwebsite.dto.QuestionCreationDTO;
import org.springframework.transaction.annotation.Transactional;
import com.freeuni.proj_100.quizwebsite.model.AnswerEntity;

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
    public Optional<Quiz> getQuizById(Integer id) {
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
    public List<Quiz> getQuizzesByCreator(Integer creatorId) {
        return quizRepository.findByCreatorId(creatorId);
    }

    /**
     * Translates a QuizCreationDTO from React, saves the parent quiz,
     * and maps/saves its constituent questions and answers.
     */
    @Transactional
    public void saveQuizFromDTO(QuizCreationDTO dto, Integer creatorId) {
        //Create the Quiz entity
        Quiz quiz = new Quiz();
        quiz.setTitle(dto.getTitle());
        quiz.setDescription(dto.getDescription());
        quiz.setRandomized(dto.isRandomizeQuestions());
        quiz.setSinglePage(dto.isSinglePageLayout());
        quiz.setImmediateFeedback(dto.isImmediateFeedback());
        quiz.setAllowPractice(dto.isAllowPractice());
        quiz.setCreatedAt(java.time.LocalDateTime.now());
        quiz.setCreatorId(creatorId);

        int sequence = 1;
        for (QuestionCreationDTO qDto : dto.getQuestions()) {
            //Create and configure Question
            QuestionEntity question = new QuestionEntity();
            question.setQuiz(quiz); // Set the parent relationship
            question.setQType(qDto.getType());
            question.setPrompt(qDto.getQuestionText());
            question.setImageUrl(qDto.getImageUrl());
            question.setSequenceNum(sequence++);

            // Configure Answers for this question
            if ("multiple-choice".equals(qDto.getType()) && qDto.getOptions() != null) {
                for (int i = 0; i < qDto.getOptions().size(); i++) {
                    String optionText = qDto.getOptions().get(i);
                    AnswerEntity answer = new AnswerEntity();
                    answer.setQuestion(question);
                    answer.setAnswerText(optionText);
                    answer.setSlotNum(i + 1);
                    answer.setCorrect(optionText.equals(qDto.getCorrectAnswer()));
                    question.getAnswers().add(answer); // Add to the list
                }
            } else if (qDto.getCorrectAnswer() != null) {
                AnswerEntity answer = new AnswerEntity();
                answer.setQuestion(question);
                answer.setAnswerText(qDto.getCorrectAnswer());
                answer.setSlotNum(1);
                answer.setCorrect(true);
                question.getAnswers().add(answer);
            }

            //Add question to quiz (cascading handles the save)
            quiz.getQuestions().add(question);
        }

        //Single repository call replaces all loop-based saves
        quizRepository.save(quiz);
    }
}