package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.model.AnswerEntity;
import com.freeuni.proj_100.quizwebsite.model.Question;
import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import com.freeuni.proj_100.quizwebsite.model.QuestionFactory;
import com.freeuni.proj_100.quizwebsite.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for loading quiz questions and evaluating submitted answers.
 */
@Service
public class QuestionService {
    /**
     * Repository used to fetch question entities together with their answers.
     */
    private final QuestionRepository questionRepository;

    /**
     * Creates a question service with its repository dependency.
     *
     * @param questionRepository repository for question persistence.
     */
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Loads all questions for a quiz and converts them into runtime Question objects.
     *
     * @param quizId parent quiz identifier.
     * @param randomized whether returned question order should be shuffled.
     * @return list of runtime question objects.
     */
    @Transactional(readOnly = true)
    public List<Question> getQuestionsForQuiz(int quizId, boolean randomized) {
        List<Question> questions = new ArrayList<>(
                questionRepository.findByQuizIdOrderBySequenceNumAscIdAsc(quizId)
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
     * Scores a quiz submission by checking each submitted answer.
     *
     * @param quizId parent quiz identifier.
     * @param params submitted request parameters keyed by "question_[id]".
     * @return number of correctly answered questions.
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
     * Converts a database question entity into the matching runtime Question implementation.
     *
     * @param entity database question entity with answers already loaded.
     * @return runtime question object.
     */
    private Question toQuestion(QuestionEntity entity) {
        List<AnswerEntity> answers = entity.getAnswers() == null
                ? List.of()
                : entity.getAnswers();

        List<String> correctAnswers = answers.stream()
                .filter(AnswerEntity::isCorrect)
                .map(AnswerEntity::getAnswerText)
                .toList();

        List<String> options = "MULTIPLE_CHOICE".equalsIgnoreCase(entity.getQ_type())
                ? answers.stream().map(AnswerEntity::getAnswerText).toList()
                : List.of();

        return QuestionFactory.createQuestion(entity, correctAnswers, options);
    }
}