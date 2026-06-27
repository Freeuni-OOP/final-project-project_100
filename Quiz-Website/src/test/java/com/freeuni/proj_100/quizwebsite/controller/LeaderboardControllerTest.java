package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.QuizAttemptDTO;
import com.freeuni.proj_100.quizwebsite.security.JwtUtil;
import com.freeuni.proj_100.quizwebsite.service.LeaderboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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
    private LeaderboardService leaderboardService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void testReturnsPopulatedLeaderboard() throws Exception {
        QuizAttemptDTO attempt = new QuizAttemptDTO(1L, "player1", 5L, 100, 45, LocalDateTime.now());

        when(leaderboardService.getTopLeaderboard(5L, 50)).thenReturn(List.of(attempt));

        mockMvc.perform(get("/api/quizzes/5/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].score").value(100))
                .andExpect(jsonPath("$[0].username").value("player1"));
    }

    @Test
    @WithMockUser
    void testReturnsEmptyLeaderboard() throws Exception {
        when(leaderboardService.getTopLeaderboard(99L, 50)).thenReturn(List.of());

        mockMvc.perform(get("/api/quizzes/99/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}