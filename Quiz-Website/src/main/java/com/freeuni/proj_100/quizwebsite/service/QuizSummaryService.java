package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.QuizAttemptDTO;
import com.freeuni.proj_100.quizwebsite.dto.QuizSummaryDTO;
import com.freeuni.proj_100.quizwebsite.repository.QuizAttemptRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QuizSummaryService {

    private final QuizAttemptRepository attemptRepository;

    public QuizSummaryService(QuizAttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    public QuizSummaryDTO getQuizSummary(Integer quizId, String username) {
        QuizSummaryDTO summary = new QuizSummaryDTO();
        var limitTen = PageRequest.of(0, 10);
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        summary.setTopAllTime(attemptRepository.getTopPerformersAllTime(quizId, limitTen)
                .stream().map(QuizAttemptDTO::new).toList());

        summary.setTopLastDay(attemptRepository.getTopPerformersLastDay(quizId, yesterday, limitTen)
                .stream().map(QuizAttemptDTO::new).toList());

        summary.setRecentTakers(attemptRepository.getRecentTestTakers(quizId, limitTen)
                .stream().map(QuizAttemptDTO::new).toList());

        summary.setUserHistory(attemptRepository.getUserPastPerformance(quizId, username)
                .stream().map(QuizAttemptDTO::new).toList());

        return summary;
    }
}