package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friendship, Long> {

    @Query(value = "SELECT COUNT(*) > 0 FROM friendships WHERE user_id = ?1 AND friend_id = ?2 AND status = 'accepted'", nativeQuery = true)
    boolean checkIfFriends(int userId, int friendId);

    @Query(value = "SELECT friend_id FROM friendships WHERE user_id = ?1 AND status = 'accepted'", nativeQuery = true)
    List<Integer> findAcceptedFriendsIds(int userId);

    @Query(value = "SELECT user_id FROM friendships WHERE friend_id = ?1 AND status = 'pending'", nativeQuery = true)
    List<Integer> findPendingRequestsForUser(int userId);
}
