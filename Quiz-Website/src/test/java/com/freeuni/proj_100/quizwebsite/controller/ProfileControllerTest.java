package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.ProfileResponseDTO;
import com.freeuni.proj_100.quizwebsite.security.JwtUtil;
import com.freeuni.proj_100.quizwebsite.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
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
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void testReturns404WhenUserIsMissing() throws Exception {
        when(profileService.getProfileData(eq("ghost"), any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profiles/ghost"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin")
    void testViewsOwnProfileAsSelf() throws Exception {
        ProfileResponseDTO selfProfile = new ProfileResponseDTO(
                "admin", "admin@test.com", true, LocalDateTime.now(), "self", List.of(), List.of()
        );

        when(profileService.getProfileData(eq("admin"), any())).thenReturn(Optional.of(selfProfile));

        mockMvc.perform(get("/api/profiles/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relation").value("self"))
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    @WithMockUser(username = "visitor")
    void testViewsOtherProfileAsViewer() throws Exception {
        ProfileResponseDTO viewerProfile = new ProfileResponseDTO(
                "targetUser", "target@test.com", false, LocalDateTime.now(), "viewer", List.of(), List.of()
        );

        when(profileService.getProfileData(eq("targetUser"), any())).thenReturn(Optional.of(viewerProfile));

        mockMvc.perform(get("/api/profiles/targetUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relation").value("viewer"));
    }

}