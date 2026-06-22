package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.model.AnswerEntity;
import com.freeuni.proj_100.quizwebsite.model.Question;
import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import com.freeuni.proj_100.quizwebsite.model.QuestionFactory;
import com.freeuni.proj_100.quizwebsite.repository.AnswerRepository;
import com.freeuni.proj_100.quizwebsite.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for loading questions and evaluating quizzes.
 */
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    /**
     * Constructs a QuestionService with its dependencies.
     *
     * @param questionRepository repository for question entities
     * @param answerRepository repository for answer entities
     */
    public QuestionService(QuestionRepository questionRepository,
                           AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    /**
     * Retrieves all questions belonging to a quiz.
     *
     * Optionally randomizes their order.
     *
     * @param quizId ID of the quiz
     * @param randomized true if question order should be shuffled
     * @return list of Question objects
     */
    @Transactional(readOnly = true)
    public List<Question> getQuestionsForQuiz(int quizId, boolean randomized) {

        List<Question> questions = new ArrayList<>(
                questionRepository.findAllForQuiz(quizId)
                        .stream()
                        .map(this::toQuestion)
                        .toList()
        );

        if (randomized) {
            Collections.shuffle(questions);
        }

        return questions;
    }

    /**
     * Computes the score obtained by a user.
     *
     * Each question checks whether the submitted parameters
     * correspond to its correct answer(s).
     *
     * @param quizId ID of the quiz
     * @param params request parameters containing user answers
     * @return number of correctly answered questions
     */
    @Transactional(readOnly = true)
    public int scoreQuiz(int quizId, Map<String, String[]> params) {

        int score = 0;

        for (Question question : getQuestionsForQuiz(quizId, false)) {
            if (question.checkAnswer(params)) {
                score++;
            }
        }

        return score;
    }

    /**
     * Converts a database entity into a domain Question object.
     *
     * Loads correct answers and, for multiple-choice questions,
     * loads all available options.
     *
     * @param entity question entity
     * @return constructed Question object
     */
    private Question toQuestion(QuestionEntity entity) {

        List<String> correctAnswers =
                answerRepository.findCorrectForQuestion(entity.getId())
                        .stream()
                        .map(AnswerEntity::getAnswerText)
                        .toList();

        List<String> options =
                "MULTIPLE_CHOICE".equalsIgnoreCase(entity.getQ_type())
                        ? answerRepository.findAllForQuestion(entity.getId())
                        .stream()
                        .map(AnswerEntity::getAnswerText)
                        .toList()
                        : List.of();

        return QuestionFactory.createQuestion(entity, correctAnswers, options);
    }
}