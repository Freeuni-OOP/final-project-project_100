package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.Friendship;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for FriendshipRepository custom queries against an in-memory H2 database.
 */

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;REFERENTIAL_INTEGRITY=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
public class FriendshipRepositoryTest {

    @Autowired
    private FriendshipRepository repository;

    /**
     * Test Queries that return List<Integer>
     */
    @Test
    public void testFindAcceptedFriendsIds() {
        // Part 1: Insert dummy data
        Friendship friend1 = new Friendship(1L, 2L, "accepted"); // Friend
        Friendship friend2 = new Friendship(3L, 1L, "accepted"); // Friend
        Friendship pending1 = new Friendship(1L, 4L, "pending"); // Not Friend yet, only pending
        Friendship notRelatedTo1 = new Friendship(5L, 6L, "accepted"); // 1 is not here

        repository.save(friend1);
        repository.save(friend2);
        repository.save(pending1);
        repository.save(notRelatedTo1);

        // Part 2: call the function
        List<Long> acceptedFriendsList = repository.findAcceptedFriendsIds(1L);

        // Part 3: Assert the List
        assertEquals(2, acceptedFriendsList.size()); // Check that user 1 has 2 friends
        assertTrue(acceptedFriendsList.contains(2L)); // is friend with user 2
        assertTrue(acceptedFriendsList.contains(3L)); // is friend with user 3
        assertFalse(acceptedFriendsList.contains(4L)); // is not friend with user 4

    }

    @Test
    public void testFindPendingRequestsForUser() {
        // Part 1: Insert dummy data
        Friendship pending1 = new Friendship(2L, 1L, "pending"); // request to 1
        Friendship pending2 = new Friendship(3L, 1L, "pending"); // request to 1
        Friendship pendingFromUser1 = new Friendship(1L, 4L, "pending"); // request from 1, shouldn't count
        Friendship friend1 = new Friendship(5L, 1L, "accepted"); // friend with 1, shouldn't count

        repository.save(pending1);
        repository.save(pending2);
        repository.save(pendingFromUser1);
        repository.save(friend1);

        // Part 2: call the function
        List<Long> pendingRequestsToUser1 = repository.findPendingRequestsForUser(1L);

        // Part 3: Assert the list
        assertEquals(2, pendingRequestsToUser1.size()); // User 1 has 2 requests
        assertTrue(pendingRequestsToUser1.contains(2L)); // has request from 2
        assertTrue(pendingRequestsToUser1.contains(3L)); // has request from 3
        assertFalse(pendingRequestsToUser1.contains(4L)); // Doesn't have request from 4
        assertFalse(pendingRequestsToUser1.contains(5L)); // Doesn't have request from 5

    }

    /**
     * Test Query that returns boolean
     */
    @Test
    public void testCheckIfFriends() {
        // Part 1: Insert dummy data
        Friendship friends = new Friendship(1L, 2L, "accepted");
        Friendship notFriendsYet = new Friendship(2L, 3L, "pending");

        repository.save(friends);
        repository.save(notFriendsYet);

        // Part 2: call the function
        boolean are1And2Friends = repository.checkIfFriends(1L, 2L);
        boolean are2And1Friends = repository.checkIfFriends(2L, 1L);
        boolean are1And3Friends = repository.checkIfFriends(1L, 3L);
        boolean are2And3Friends = repository.checkIfFriends(2L, 3L);

        // Part 3: Assert correct friendships
        assertTrue(are1And2Friends); // 1 and 2 should be friends
        assertTrue(are2And1Friends); // 1 and 2 should also work backwards
        assertFalse(are1And3Friends); // 1 and 3 shouldn't be friends
        assertFalse(are2And3Friends); // 2 and 3 shouldn't be friends

    }

}
