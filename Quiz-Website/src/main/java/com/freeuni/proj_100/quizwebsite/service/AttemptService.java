package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.AttemptResultDto;
import com.freeuni.proj_100.quizwebsite.dto.AttemptSubmitRequest;
import com.freeuni.proj_100.quizwebsite.exception.ResourceNotFoundException;
import com.freeuni.proj_100.quizwebsite.model.AnswerEntity;
import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.repository.QuizAttemptRepository;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttemptService {

    private final QuizService quizService;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;

    public AttemptService(QuizService quizService,
                          QuizAttemptRepository quizAttemptRepository,
                          UserRepository userRepository) {
        this.quizService = quizService;
        this.quizAttemptRepository = quizAttemptRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AttemptResultDto submitAttempt(
            Integer quizId,
            AttemptSubmitRequest request,
            String username
    ) {
        var quiz = quizService.getQuizForTaking(quizId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Quiz not found with id: " + quizId));

        Map<Integer, String> userAnswers = request.getAnswers().stream()
                .collect(Collectors.toMap(
                        AttemptSubmitRequest.AnswerSubmission::getQuestionId,
                        AttemptSubmitRequest.AnswerSubmission::getResponse
                ));

        int score = 0;
        List<AttemptResultDto.QuestionResultDto> results = new ArrayList<>();

        for (QuestionEntity question : quiz.getQuestions()) {
            String userAnswer = userAnswers.getOrDefault(
                    question.getId(), "");
            String correctAnswer = question.getAnswers().stream()
                    .filter(AnswerEntity::isCorrect)
                    .map(AnswerEntity::getAnswerText)
                    .findFirst()
                    .orElse("");

            boolean correct = correctAnswer.equalsIgnoreCase(userAnswer.trim());
            if (correct) score++;

            results.add(new AttemptResultDto.QuestionResultDto(
                    question.getId(),
                    correct,
                    correctAnswer,
                    userAnswer
            ));
        }

        // save attempt unless practice mode
        if (!request.isPractice()) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            QuizAttempt attempt = new QuizAttempt();
            attempt.setUser(user);
            attempt.setQuizId(quizId);
            attempt.setScore(score);
            attempt.setTimeTakenSec(request.getTimeTakenSec());
            attempt.setPractice(false);
            quizAttemptRepository.save(attempt);
        }

        return new AttemptResultDto(
                score,
                quiz.getQuestions().size(),
                request.getTimeTakenSec(),
                results
        );
    }
}