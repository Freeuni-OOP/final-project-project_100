package com.freeuni.proj_100.quizwebsite.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key class for the Friendship entity.
 * Represents the unique (user_id, friend_id) pair in the database.
 */
public class FriendshipId implements Serializable {

    private Long userId;
    private Long friendId;

    public FriendshipId(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public FriendshipId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipId that = (FriendshipId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(friendId, that.friendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId);
    }
}