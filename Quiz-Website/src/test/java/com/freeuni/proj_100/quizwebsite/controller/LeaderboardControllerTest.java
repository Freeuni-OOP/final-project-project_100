package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import com.freeuni.proj_100.quizwebsite.repository.QuizAttemptRepository;
import com.freeuni.proj_100.quizwebsite.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeaderboardController.class)
class LeaderboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuizAttemptRepository repository;

    @MockitoBean
    private JwtUtil jwtUtil; // bypass security filter crash

    @Test
    @WithMockUser
    void returnsPopulatedLeaderboard() throws Exception {
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUserId(1L);
        attempt.setScore(100);

        // simulate db returning 1 attempt
        when(repository.getLeaderboard(5)).thenReturn(List.of(attempt));

        mockMvc.perform(get("/api/quizzes/5/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].score").value(100));
    }

    @Test
    @WithMockUser
    void returnsEmptyLeaderboard() throws Exception {
        // simulate empty db response for a brand new quiz
        when(repository.getLeaderboard(99)).thenReturn(List.of());

        mockMvc.perform(get("/api/quizzes/99/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0)); // expect empty json array
    }
}