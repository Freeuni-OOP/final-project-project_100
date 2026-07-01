package com.freeuni.proj_100.quizwebsite.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key class for the Friendship entity.
 * Represents the (user_id, friend_id) primary key pair in the friendships table.
 * Required by JPA when using @IdClass on an entity with a composite key.
 * Must implement Serializable as per the JPA specification.
 */
public class FriendshipId implements Serializable {

    /// Variable Declarations
    private Long user_id;
    private Long friend_id;

    /// Constructor
    public FriendshipId(Long user_id, Long friend_id) {
        this.user_id = user_id;
        this.friend_id = friend_id;
    }

    /**
     * No-arg constructor required by JPA for reflective instantiation.
     */
    public FriendshipId() {}

    /**
     * Two FriendshipId objects are equal if and only if both user_id and friend_id match.
     * Required by JPA to correctly identify and compare composite keys.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipId that = (FriendshipId) o;
        return Objects.equals(user_id, that.user_id) &&
                Objects.equals(friend_id, that.friend_id);
    }

    /**
     * Consistent with equals — two equal FriendshipIds will produce the same hash.
     * Required by JPA for correct behavior in hash-based collections and caches.
     */
    @Override
    public int hashCode() {
        return Objects.hash(user_id, friend_id);
    }

}
