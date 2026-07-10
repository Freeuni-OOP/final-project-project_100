package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a friendship or a pending friend request between two users.
 * Maps to the 'friendships' table using a composite primary key.
 */
@Entity
@Table(name="friendships")
@IdClass(FriendshipId.class)
public class Friendship {

    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Id
    @Column(name = "friend_id", nullable = false)
    private Integer friendId;

    @Column(name = "status", nullable = false)
    private FriendshipStatus status;

    // Database handles generation: DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    @Column(name = "established_at", insertable = false, updatable = false)
    private LocalDateTime establishedAt;

    public Friendship(Integer userId, Integer friendId, FriendshipStatus status) {
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
    }

    public Friendship() {}

    public Integer getUserId() { return userId; }
    public Integer getFriendId() { return friendId; }
    public FriendshipStatus getStatus() { return status; }
    public LocalDateTime getEstablishedAt() { return establishedAt; }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }
}