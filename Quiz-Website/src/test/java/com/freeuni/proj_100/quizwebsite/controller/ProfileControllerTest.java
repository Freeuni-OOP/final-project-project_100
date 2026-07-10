package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.ProfileResponseDTO;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import com.freeuni.proj_100.quizwebsite.security.JwtUtil;
import com.freeuni.proj_100.quizwebsite.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileService profileService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "tazo")
    void testGetProfileSuccessfullyAndVerifiesPayload() throws Exception {
        ProfileResponseDTO mockProfile = new ProfileResponseDTO(
                1, // Updated to primitive int for the ID match
                "tazo",
                "tazo@example.com",
                false,
                LocalDateTime.now(),
                "SELF",
                List.of(),
                List.of()
        );

        when(profileService.getProfileData(eq("tazo"), any())).thenReturn(Optional.of(mockProfile));

        mockMvc.perform(get("/api/profiles/tazo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1)) // Updated check for primitive int
                .andExpect(jsonPath("$.username").value("tazo"))
                .andExpect(jsonPath("$.email").value("tazo@example.com"))
                .andExpect(jsonPath("$.isAdmin").value(false))
                .andExpect(jsonPath("$.relation").value("SELF"));
    }

    @Test
    @WithMockUser(username = "tazo")
    void testGetProfileNotFound() throws Exception {
        when(profileService.getProfileData(eq("unknown_user"), any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profiles/unknown_user"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFailsIfUserNotAuthenticated() throws Exception {
        // No @WithMockUser annotation, testing raw anonymous access
        mockMvc.perform(get("/api/profiles/tazo"))
                .andExpect(status().isUnauthorized());
    }
}