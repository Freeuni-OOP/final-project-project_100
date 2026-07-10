package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.Friendship;
import com.freeuni.proj_100.quizwebsite.model.FriendshipStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;REFERENTIAL_INTEGRITY=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
class FriendshipRepositoryTest {

    @Autowired
    private FriendshipRepository repository;

    @Test
    void testFindFriendshipBetweenUsers() {
        Friendship f1 = new Friendship(1, 2, FriendshipStatus.PENDING);
        repository.save(f1);

        // Should find regardless of order
        Optional<Friendship> result1 = repository.findFriendshipBetween(1, 2);
        Optional<Friendship> result2 = repository.findFriendshipBetween(2, 1);

        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertEquals(FriendshipStatus.PENDING, result1.get().getStatus());
    }

    @Test
    void testFindAcceptedFriendsIds() {
        repository.save(new Friendship(1, 2, FriendshipStatus.ACCEPTED)); // 1 and 2 are friends
        repository.save(new Friendship(3, 1, FriendshipStatus.ACCEPTED)); // 3 and 1 are friends
        repository.save(new Friendship(1, 4, FriendshipStatus.PENDING));  // 1 and 4 are NOT friends

        List<Integer> friendsOfUser1 = repository.findFriendsIdsByStatus(1, FriendshipStatus.ACCEPTED);

        assertEquals(2, friendsOfUser1.size());
        assertTrue(friendsOfUser1.contains(2));
        assertTrue(friendsOfUser1.contains(3));
        assertFalse(friendsOfUser1.contains(4));
    }

    @Test
    void testFindIncomingRequests() {
        repository.save(new Friendship(2, 1, FriendshipStatus.PENDING)); // 2 sent to 1
        repository.save(new Friendship(3, 1, FriendshipStatus.PENDING)); // 3 sent to 1
        repository.save(new Friendship(1, 4, FriendshipStatus.PENDING)); // 1 sent to 4 (Outgoing, should not count)

        List<Integer> requestsForUser1 = repository.findIncomingRequests(1, FriendshipStatus.PENDING);

        assertEquals(2, requestsForUser1.size());
        assertTrue(requestsForUser1.contains(2));
        assertTrue(requestsForUser1.contains(3));
    }
}