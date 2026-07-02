package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.QuizSummaryDTO;
import com.freeuni.proj_100.quizwebsite.repository.QuizAttemptRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizSummaryServiceTest {

    @Mock
    private QuizAttemptRepository attemptRepository;

    @InjectMocks
    private QuizSummaryService quizSummaryService;

    @Test
    void testGetQuizSummaryOrchestration() {
        var limitTen = PageRequest.of(0, 10);

        // Stub out the 4 internal queries to return empty mock lists instantly
        when(attemptRepository.getTopPerformersAllTime(1L, limitTen)).thenReturn(List.of());
        when(attemptRepository.getTopPerformersLastDay(eq(1L), any(LocalDateTime.class), eq(limitTen))).thenReturn(List.of());
        when(attemptRepository.getRecentTestTakers(1L, limitTen)).thenReturn(List.of());
        when(attemptRepository.getUserPastPerformance(1L, "tazo")).thenReturn(List.of());

        // Execute the service layer
        QuizSummaryDTO summary = quizSummaryService.getQuizSummary(1L, "tazo");

        // Verify the response container was built smoothly
        assertNotNull(summary);

        // Verify that the service accurately invoked all 4 custom queries
        verify(attemptRepository).getTopPerformersAllTime(1L, limitTen);
        verify(attemptRepository).getTopPerformersLastDay(eq(1L), any(LocalDateTime.class), eq(limitTen));
        verify(attemptRepository).getRecentTestTakers(1L, limitTen);
        verify(attemptRepository).getUserPastPerformance(1L, "tazo");
    }
}