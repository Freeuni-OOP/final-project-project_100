package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.repository.QuizAttemptRepository;
import com.freeuni.proj_100.quizwebsite.repository.UserAchievementRepository;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import com.freeuni.proj_100.quizwebsite.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private QuizAttemptRepository attemptRepository;

    @MockitoBean
    private UserAchievementRepository achievementRepository;

    @MockitoBean
    private JwtUtil jwtUtil; // bypass security filter crash

    @Test
    @WithMockUser
    void returns404IfUserNotFound() throws Exception {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profiles/ghost"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin") // simulated login as admin
    void returnsProfileAsSelf() throws Exception {
        User target = new User();
        target.setId(1L);
        target.setUsername("admin");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(target));
        when(attemptRepository.getUserHistory(1L)).thenReturn(List.of());
        when(achievementRepository.findByUserId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/profiles/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relation").value("self")) // logic check 1
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    @WithMockUser(username = "visitor") // simulated login as someone else
    void returnsProfileAsViewer() throws Exception {
        User target = new User();
        target.setId(2L);
        target.setUsername("targetUser");

        when(userRepository.findByUsername("targetUser")).thenReturn(Optional.of(target));
        when(attemptRepository.getUserHistory(2L)).thenReturn(List.of());
        when(achievementRepository.findByUserId(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/profiles/targetUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relation").value("viewer")); // logic check 2
    }
}