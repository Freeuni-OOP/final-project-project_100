package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.FriendshipResponseDTO;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import com.freeuni.proj_100.quizwebsite.security.JwtUtil;
import com.freeuni.proj_100.quizwebsite.service.FriendshipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FriendshipController.class)
class FriendshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FriendshipService friendshipService;

    // Required to prevent JwtAuthFilter from crashing
    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "tazo")
    void testGetAcceptedFriendsReturnsList() throws Exception {
        FriendshipResponseDTO friend = new FriendshipResponseDTO(2, "john", "ACCEPTED");
        when(friendshipService.getAcceptedFriends("tazo")).thenReturn(List.of(friend));

        mockMvc.perform(get("/api/friends/accepted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").value("john"))
                .andExpect(jsonPath("$[0].status").value("ACCEPTED"));
    }

    @Test
    @WithMockUser(username = "tazo")
    void testSendRequestReturnsOk() throws Exception {
        // Appended .with(csrf()) to satisfy Spring Security
        mockMvc.perform(post("/api/friends/request/john").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tazo")
    void testRejectRequestReturnsOk() throws Exception {
        // Appended .with(csrf()) to satisfy Spring Security
        mockMvc.perform(put("/api/friends/reject/2").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void testFailsIfUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/friends/accepted"))
                .andExpect(status().isUnauthorized());
    }
}