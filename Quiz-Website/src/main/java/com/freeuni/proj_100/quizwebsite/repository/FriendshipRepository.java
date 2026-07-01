package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.Friendship;
import com.freeuni.proj_100.quizwebsite.model.FriendshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for the Friendship entity, providing database access for the 'friendships' table.
 * Extends JpaRepository for standard CRUD operations, and defines three custom native queries:
 *    -check if two users are friends
 *    -return ids of users that user is a friend with
 *    -return ids of users that user has pending requests from
 */
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    /// Checks if 'userId' and 'friendId' are friends
    @Query(value = "SELECT EXISTS(SELECT 1 FROM friendships WHERE (user_id = ?1 AND friend_id = ?2 OR user_id = ?2 AND friend_id = ?1) AND status = 'accepted')", nativeQuery = true)
    boolean checkIfFriends(Long userId, Long friendId);

    /// Returns all ids of users that 'userId' is friends with
    @Query(value = "SELECT friend_id FROM friendships WHERE user_id = ?1 AND status = 'accepted' UNION SELECT user_id FROM friendships WHERE friend_id = ?1 AND status = 'accepted'", nativeQuery = true)
    List<Long> findAcceptedFriendsIds(Long userId);

    /// Returns all ids of users that 'userId' has received friend request from
    @Query(value = "SELECT user_id FROM friendships WHERE friend_id = ?1 AND status = 'pending'", nativeQuery = true)
    List<Long> findPendingRequestsForUser(Long userId);
}
