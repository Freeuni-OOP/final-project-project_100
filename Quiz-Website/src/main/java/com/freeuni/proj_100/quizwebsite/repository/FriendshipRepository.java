package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.Friendship;
import com.freeuni.proj_100.quizwebsite.model.FriendshipId;
import com.freeuni.proj_100.quizwebsite.model.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Friendship data access operations.
 */
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    /**
     * Checks if a friendship mapping exists between two users in either direction.
     */
    @Query("SELECT f FROM Friendship f WHERE (f.userId = :user1 AND f.friendId = :user2) OR (f.userId = :user2 AND f.friendId = :user1)")
    Optional<Friendship> findFriendshipBetween(@Param("user1") Integer user1, @Param("user2") Integer user2);

    /**
     * Returns a list of User IDs that the provided user is friends with (based on status).
     * The CASE statement extracts the ID of the *other* person in the relationship.
     */
    @Query("SELECT CASE WHEN f.userId = :userId THEN f.friendId ELSE f.userId END FROM Friendship f WHERE (f.userId = :userId OR f.friendId = :userId) AND f.status = :status")
    List<Integer> findFriendsIdsByStatus(@Param("userId") Integer userId, @Param("status") FriendshipStatus status);

    /**
     * Returns a list of User IDs who sent a request TO the provided user (based on status).
     */
    @Query("SELECT f.userId FROM Friendship f WHERE f.friendId = :userId AND f.status = :status")
    List<Integer> findIncomingRequests(@Param("userId") Integer userId, @Param("status") FriendshipStatus status);
}