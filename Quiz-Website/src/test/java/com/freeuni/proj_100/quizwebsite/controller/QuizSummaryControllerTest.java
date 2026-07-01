package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.QuizSummaryDTO;
import com.freeuni.proj_100.quizwebsite.security.JwtUtil;
import com.freeuni.proj_100.quizwebsite.service.QuizSummaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuizSummaryController.class)
class QuizSummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuizSummaryService quizSummaryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "tazo")
    void testReturnsQuizSummarySuccessfully() throws Exception {
        // Using the empty constructor as defined in your DTO class
        QuizSummaryDTO mockSummary = new QuizSummaryDTO();

        when(quizSummaryService.getQuizSummary(1L, "tazo")).thenReturn(mockSummary);

        mockMvc.perform(get("/api/quizzes/1/summary"))
                .andExpect(status().isOk());
    }

    @Test
    void testFailsIfUserNotAuthenticated() throws Exception {
        // No @WithMockUser annotation means this request is anonymous
        mockMvc.perform(get("/api/quizzes/1/summary"))
                .andExpect(status().isUnauthorized());
    }
}