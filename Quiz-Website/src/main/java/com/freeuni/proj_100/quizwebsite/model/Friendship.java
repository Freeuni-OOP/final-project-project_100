package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;

/**
 * Entity representing a friendship or a friend request between two users
 * Maps to 'friendships' table, that uses composite key: user_id, friend_id
 *
 * friendship can have two states
 *    -"pending": user_id has sent friend request to friend_id and friend_id hasn't accepted yet.
 *    -"accepted": users are confirmed friends
 */
@Entity
@Table(name="friendships")
@IdClass(FriendshipId.class)
public class Friendship {

    /// Variable Declarations
    @Id
    @Column(name = "user_id")
    private Long user_id;

    @Id
    @Column(name = "friend_id")
    private Long friend_id;

    @Column(name = "status")
    private String status;


    /// Constructor
    public Friendship(Long user_id, Long friend_id, String status) {
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.status = status;
    }

    /// Getters
    public Long getUser1()    {return user_id;}
    public Long getUser2()    {return friend_id;}
    public String getStatus() {return status;}


    /// Setter
    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * No-arg constructor required by JPA for reflective instantiation.
     */
    public Friendship() {}
}
