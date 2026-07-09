package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.model.Friendship;
import com.freeuni.proj_100.quizwebsite.model.FriendshipStatus;
import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.repository.FriendshipRepository;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceTest {

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendshipService friendshipService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(); user1.setId(1L); user1.setUsername("tazo");
        user2 = new User(); user2.setId(2L); user2.setUsername("john");
    }

    @Test
    void testSendFriendRequestSuccessfully() {
        when(userRepository.findByUsername("tazo")).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user2));
        when(friendshipRepository.findFriendshipBetween(1L, 2L)).thenReturn(Optional.empty());

        friendshipService.sendFriendRequest("tazo", "john");

        verify(friendshipRepository, times(1)).save(any(Friendship.class));
    }

    @Test
    void testSendFriendRequestFailsIfSelf() {
        when(userRepository.findByUsername("tazo")).thenReturn(Optional.of(user1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            friendshipService.sendFriendRequest("tazo", "tazo");
        });

        assertEquals("You cannot send a friend request to yourself.", exception.getMessage());
    }

    @Test
    void testSendFriendRequestFailsIfBlockedByRejection() {
        when(userRepository.findByUsername("tazo")).thenReturn(Optional.of(user1)); // Sender
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user2)); // Target

        // Simulate that user1 previously sent a request, and user2 rejected it
        Friendship rejectedContext = new Friendship(1L, 2L, FriendshipStatus.REJECTED);
        when(friendshipRepository.findFriendshipBetween(1L, 2L)).thenReturn(Optional.of(rejectedContext));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            friendshipService.sendFriendRequest("tazo", "john");
        });

        assertEquals("You cannot send a friend request to this user.", exception.getMessage());
    }

    @Test
    void testAcceptFriendRequestFailsIfSenderTriesToAccept() {
        when(userRepository.findByUsername("tazo")).thenReturn(Optional.of(user1)); // Current user

        // Simulate a request where user1 (tazo) is the sender (userId = 1L)
        Friendship pendingRequest = new Friendship(1L, 2L, FriendshipStatus.PENDING);
        when(friendshipRepository.findFriendshipBetween(2L, 1L)).thenReturn(Optional.of(pendingRequest));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            friendshipService.acceptFriendRequest("tazo", 2L);
        });

        assertEquals("Unauthorized relationship manipulation.", exception.getMessage());
    }
}