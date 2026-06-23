package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.QuizAttemptDTO;
import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import com.freeuni.proj_100.quizwebsite.repository.QuizAttemptRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService {

    private final QuizAttemptRepository attemptRepository;

    public LeaderboardService(QuizAttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    public List<QuizAttemptDTO> getTopLeaderboard(Long quizId, int limit) {
        return attemptRepository.getLeaderboard(quizId, PageRequest.of(0, limit))
                .stream()
                .map(this::toDTO) // Points to the helper method below
                .toList();
    }

    // HELPER METHOD: Keeps the stream clean
    private QuizAttemptDTO toDTO(QuizAttempt a) {
        return new QuizAttemptDTO(
                a.getId(),
                a.getUser().getUsername(),
                a.getQuizId(),
                a.getScore(),
                a.getTimeTakenSec(),
                a.getTakenAt()
        );
    }
}