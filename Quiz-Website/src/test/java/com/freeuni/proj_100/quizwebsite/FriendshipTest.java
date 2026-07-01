package com.freeuni.proj_100.quizwebsite;

import com.freeuni.proj_100.quizwebsite.model.Friendship;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Friendship entity — constructor, getters, and setters.
 */
public class FriendshipTest {

    @Test
    public void testConstructorGettersAndSetters() {

        Friendship frnd = new Friendship(4L, 5L, "Accepted");


        // GETTERS TEST
        assertEquals(4L, frnd.getUser1());
        assertEquals(5L, frnd.getUser2());
        assertEquals("Accepted", frnd.getStatus());

        // SETTER TEST
        frnd.setStatus("Pending");
        assertEquals("Pending", frnd.getStatus());

    };
}
