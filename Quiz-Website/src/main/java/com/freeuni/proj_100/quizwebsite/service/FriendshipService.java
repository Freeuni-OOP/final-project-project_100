package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.FriendshipResponseDTO;
import com.freeuni.proj_100.quizwebsite.model.Friendship;
import com.freeuni.proj_100.quizwebsite.model.FriendshipStatus;
import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.repository.FriendshipRepository;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer orchestrating the business logic for friendships.
 * Enforces anti-spam rules and state mutations.
 */
@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void sendFriendRequest(String currentUsername, String targetUsername) {
        User sender = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found."));
        User target = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new IllegalArgumentException("Target user not found."));

        if (sender.getId().equals(target.getId())) {
            throw new IllegalArgumentException("You cannot send a friend request to yourself.");
        }

        Optional<Friendship> existingOpt = friendshipRepository.findFriendshipBetween(sender.getId(), target.getId());

        if (existingOpt.isPresent()) {
            Friendship existing = existingOpt.get();

            if (existing.getStatus() == FriendshipStatus.REJECTED) {
                if (existing.getUserId().equals(sender.getId())) {
                    throw new IllegalStateException("You cannot send a friend request to this user.");
                } else {
                    friendshipRepository.delete(existing);
                    friendshipRepository.flush();
                }
            } else {
                throw new IllegalStateException("A friendship connection or active pending request already exists.");
            }
        }

        Friendship friendship = new Friendship(sender.getId(), target.getId(), FriendshipStatus.PENDING);
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void acceptFriendRequest(String currentUsername, Integer requesterId) {
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow();
        Friendship request = friendshipRepository.findFriendshipBetween(requesterId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Friend request context not found."));

        if (request.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be accepted.");
        }

        if (!request.getFriendId().equals(currentUser.getId())) {
            throw new IllegalStateException("Unauthorized relationship manipulation.");
        }

        request.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(request);
    }

    @Transactional
    public void rejectFriendRequest(String currentUsername, Integer requesterId) {
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow();
        Friendship request = friendshipRepository.findFriendshipBetween(requesterId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Friend request context not found."));

        if (request.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be rejected.");
        }

        if (!request.getFriendId().equals(currentUser.getId())) {
            throw new IllegalStateException("Unauthorized relationship manipulation.");
        }

        request.setStatus(FriendshipStatus.REJECTED);
        friendshipRepository.save(request);
    }

    @Transactional
    public void removeFriend(String currentUsername, Integer targetUserId) {
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow();
        Friendship friendship = friendshipRepository.findFriendshipBetween(currentUser.getId(), targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Target connection map does not exist."));

        friendshipRepository.delete(friendship);
    }

    public List<FriendshipResponseDTO> getAcceptedFriends(String username) {
        User currentUser = userRepository.findByUsername(username).orElseThrow();
        List<Integer> friendIds = friendshipRepository.findFriendsIdsByStatus(currentUser.getId(), FriendshipStatus.ACCEPTED);

        return userRepository.findAllById(friendIds).stream()
                .map(user -> new FriendshipResponseDTO(user.getId(), user.getUsername(), "ACCEPTED"))
                .collect(Collectors.toList());
    }

    public List<FriendshipResponseDTO> getPendingRequests(String username) {
        User currentUser = userRepository.findByUsername(username).orElseThrow();
        List<Integer> requesterIds = friendshipRepository.findIncomingRequests(currentUser.getId(), FriendshipStatus.PENDING);

        return userRepository.findAllById(requesterIds).stream()
                .map(user -> new FriendshipResponseDTO(user.getId(), user.getUsername(), "PENDING"))
                .collect(Collectors.toList());
    }
}