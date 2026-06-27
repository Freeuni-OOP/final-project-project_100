package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;

@Entity
@Table(name="friendships")
@IdClass(FriendshipId.class)
public class Friendship {


    @Id
    @Column(name = "user_id")
    private Long user_id;

    @Id
    @Column(name = "friend_id")
    private Long friend_id;

    @Column(name = "status")
    private String status;

    public Friendship() {}


    public Friendship(Long user_id, Long friend_id, String status) {
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.status = status;
    }

    public Long getUser1() {
        return user_id;
    }
    public Long getUser2() {
        return friend_id;
    }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}