package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.FriendshipResponseDTO;
import com.freeuni.proj_100.quizwebsite.service.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * REST Controller exposing friendship management actions.
 */
@RestController
@RequestMapping("/api/friends")
@CrossOrigin(origins = "http://localhost:5173")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping("/accepted")
    public ResponseEntity<List<FriendshipResponseDTO>> getFriends(Principal principal) {
        return ResponseEntity.ok(friendshipService.getAcceptedFriends(principal.getName()));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendshipResponseDTO>> getPendingRequests(Principal principal) {
        return ResponseEntity.ok(friendshipService.getPendingRequests(principal.getName()));
    }

    @PostMapping("/request/{targetUsername}")
    public ResponseEntity<String> sendRequest(@PathVariable String targetUsername, Principal principal) {
        friendshipService.sendFriendRequest(principal.getName(), targetUsername);
        return ResponseEntity.ok("Friend request sent successfully.");
    }

    @PutMapping("/accept/{requesterId}")
    public ResponseEntity<String> acceptRequest(@PathVariable Long requesterId, Principal principal) {
        friendshipService.acceptFriendRequest(principal.getName(), requesterId);
        return ResponseEntity.ok("Friend request accepted.");
    }

    @PutMapping("/reject/{requesterId}")
    public ResponseEntity<String> rejectRequest(@PathVariable Long requesterId, Principal principal) {
        friendshipService.rejectFriendRequest(principal.getName(), requesterId);
        return ResponseEntity.ok("Friend request rejected.");
    }

    @DeleteMapping("/remove/{targetUserId}")
    public ResponseEntity<String> removeFriend(@PathVariable Long targetUserId, Principal principal) {
        friendshipService.removeFriend(principal.getName(), targetUserId);
        return ResponseEntity.ok("Friend relation updated successfully.");
    }
}