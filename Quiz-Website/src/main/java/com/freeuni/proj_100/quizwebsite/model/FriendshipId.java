package com.freeuni.proj_100.quizwebsite.model;

import java.io.Serializable;
import java.util.Objects;



public class FriendshipId implements Serializable {

    private Long user_id;
    private Long friend_id;

    public FriendshipId(Long user_id, Long friend_id) {
        this.user_id = user_id;
        this.friend_id = friend_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipId that = (FriendshipId) o;
        return Objects.equals(user_id, that.user_id) &&
                Objects.equals(friend_id, that.friend_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, friend_id);
    }
}
