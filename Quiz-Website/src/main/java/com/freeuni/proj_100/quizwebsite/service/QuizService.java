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
import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import com.freeuni.proj_100.quizwebsite.repository.QuestionRepository;
import org.springframework.transaction.annotation.Transactional;
import com.freeuni.proj_100.quizwebsite.model.AnswerEntity;
import com.freeuni.proj_100.quizwebsite.model.MultipleChoiceQuestion;
import com.freeuni.proj_100.quizwebsite.model.PictureResponseQuestion;
import com.freeuni.proj_100.quizwebsite.model.FillInTheBlankQuestion;

/**
 * Service layer for quiz-related business logic.
 * Sits between the controller and repository.
 */
@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    /**
     * Spring automatically injects QuizRepository here.
     */
    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
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

    /**
     * Translates a QuizCreationDTO from React, saves the parent quiz,
     * and maps/saves its constituent questions and answers.
     */
    @Transactional
    public void saveQuizFromDTO(QuizCreationDTO dto) {
        Quiz quiz = new Quiz();
        quiz.setTitle(dto.getTitle());
        quiz.setDescription(dto.getDescription());
        quiz.setRandomized(dto.isRandomizeQuestions());
        quiz.setSinglePage(dto.isSinglePageLayout());
        quiz.setImmediateFeedback(dto.isImmediateFeedback());
        quiz.setCreatedAt(java.time.LocalDateTime.now());
        Quiz savedQuiz = quizRepository.save(quiz);

        int sequence = 1;
        for (QuestionCreationDTO qDto : dto.getQuestions()) {
            // 1. Always use QuestionEntity for database operations
            QuestionEntity question = new QuestionEntity();

            // 2. Set the data
            question.setQuizId(savedQuiz.getId().intValue());
            question.setQType(qDto.getType()); // This acts as your discriminator
            question.setPrompt(qDto.getQuestionText());
            question.setImageUrl(qDto.getImageUrl()); // Will be null for non-picture questions
            question.setSequenceNum(sequence++);

            //Save the base entity first
            QuestionEntity savedQuestion = questionRepository.save(question);

            // Handle Answer Logic
            if ("multiple-choice".equals(qDto.getType()) && qDto.getOptions() != null) {
                for (int i = 0; i < qDto.getOptions().size(); i++) {
                    String optionText = qDto.getOptions().get(i);
                    AnswerEntity answer = new AnswerEntity();
                    answer.setQuestion(savedQuestion);
                    answer.setAnswerText(optionText);
                    answer.setSlotNum(i + 1);
                    answer.setCorrect(optionText.equals(qDto.getCorrectAnswer()));
                    answerRepository.save(answer);
                }
            } else if (qDto.getCorrectAnswer() != null) {
                AnswerEntity answer = new AnswerEntity();
                answer.setQuestion(savedQuestion);
                answer.setAnswerText(qDto.getCorrectAnswer());
                answer.setSlotNum(1);
                answer.setCorrect(true);
                answerRepository.save(answer);
            }
        }
    }
}