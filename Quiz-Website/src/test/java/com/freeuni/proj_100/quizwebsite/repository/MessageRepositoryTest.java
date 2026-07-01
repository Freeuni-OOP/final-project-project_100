package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.freeuni.proj_100.quizwebsite.model.Message.MessageType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for MessageRepository custom queries against an in-memory H2 database.
 */

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;REFERENTIAL_INTEGRITY=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository repository;


    /**
    * Test functions that Return List<Long>
     * */
    @Test
    public void testGetUnreadChatIds() {
        // Part 1: Put some dummy data in
        Message unreadMsg = new Message(2L, 1L, note, "Wow"); // isRead is false by default

        Message readMsg = new Message(3L, 1L, note, "Hey");
        readMsg.setRead(true);

        repository.save(unreadMsg);
        repository.save(readMsg);

        // Part 2: Call the function
        List<Long> unreadIds = repository.getUnreadChatIds(1L); // get unread chatIds for user with id = 1

        // Part 3: Assert the list
        assertEquals(1, unreadIds.size()); // Should only contain one id
        assertTrue(unreadIds.contains(2L)); // Should contain id = 2
        assertFalse(unreadIds.contains(3L)); // Should not contain id = 4
    }

    @Test
    public void testGetAllChats() {
        // Part 1: Put some dummy data in
        Message matchReceiver = new Message(2L, 1L, note, "Hello"); // Receivers id is 1
        Message matchSender = new Message(1L, 3L, challenge, "hey"); // Senders id is 1
        Message noMatch = new Message(4L, 5L, note, "Hi"); // neither sender nor receiver has id 1
        Message matchReceiverDuplicate = new Message(2L, 1L, note, "Something"); // user already has message in this chat, shouldn't return twice

        repository.save(matchReceiver);
        repository.save(matchSender);
        repository.save(noMatch);
        repository.save(matchReceiverDuplicate);

        // Part 2: Call the function
        List<Long> allChats = repository.getAllChats(1L); // get all chat ids for user with id = 1

        // Part 3: Assert the list
        assertEquals(2, allChats.size()); // Should contain two ids, shouldn't duplicate id 2
        assertTrue(allChats.contains(2L)); // Should contain id 2
        assertTrue(allChats.contains(3L)); // Should contain id 3
        assertFalse(allChats.contains(4L)); // Shouldn't contain id 4
        assertFalse(allChats.contains(5L)); // Shouldn't contain id 5
    }

    /**
     * Test functions that Return List<Message>
     */

    @Test
    public void testGetMessages() throws InterruptedException {
        // Part 1: put some dummy data in
        Message msg1 = new Message(1L, 2L, note, "Hello number 2");
        Message msg2 = new Message(2L, 1L, note, "Hello number 1");

        Message msgNotBetween1And2 = new Message(1L, 3L, note, "Hello number 3");

        repository.save(msg1);
        Thread.sleep(10); // Force a 10ms pause, so that the timestamps are forced to be different and it doesn't break the Descending order
        repository.save(msg2);
        Thread.sleep(10); // Force a 10ms pause, so that the timestamps are forced to be different and it doesn't break the Descending order
        repository.save(msgNotBetween1And2);

        // Part 2: Call the function
        List<Message> conversation = repository.getMessages(1L, 2L);

        // Part 3: Assert the list
        assertEquals(2, conversation.size()); // only messages between 1 and 2 saved
        // The Messages are ordered Descending. Check if messages match
        assertEquals("Hello number 1", conversation.get(0).getTextContent());
        assertEquals("Hello number 2", conversation.get(1).getTextContent());
    }

    @Test
    public void testGetInboxPreviewTexts() throws InterruptedException {
        // Part 1: put some dummy data in
        Message msg1 = new Message(2L, 1L, note, "First message between 1 and 2");
        Message msg2 = new Message(2L, 1L, note, "Second message between 1 and 2");
        Message msg3 = new Message(3L, 1L, note, "First message between 1 and 3");
        Message msg4 = new Message(1L, 3L, note, "Second message between 1 and 3");
        Message msg5 = new Message(1L, 4L, note, "First and only message between 1 and 4");

        repository.save(msg1);
        Thread.sleep(10); // Force a 10ms pause, so that the timestamps are forced to be different and it doesn't break the Descending order
        repository.save(msg2);
        Thread.sleep(10); // Force a 10ms pause, so that the timestamps are forced to be different and it doesn't break the Descending order
        repository.save(msg3);
        Thread.sleep(10); // Force a 10ms pause, so that the timestamps are forced to be different and it doesn't break the Descending order
        repository.save(msg4);
        Thread.sleep(10); // Force a 10ms pause, so that the timestamps are forced to be different and it doesn't break the Descending order
        repository.save(msg5);
        Thread.sleep(10); // Force a 10ms pause, so that the timestamps are forced to be different and it doesn't break the Descending order

        // Part 2: Call the function
        List<Message> lastMessages = repository.getInboxPreviewTexts(1L);

        // Part 3: Assert the list
        assertEquals(3, lastMessages.size()); // person with id = 1 has only chatted with 3 people
        // The Messages are ordered Descending. Check if Messages match.
        assertEquals("First and only message between 1 and 4", lastMessages.get(0).getTextContent());
        assertEquals("Second message between 1 and 3", lastMessages.get(1).getTextContent());
        assertEquals("Second message between 1 and 2", lastMessages.get(2).getTextContent());
    }

    /**
     * Test function the Returns void
     */
    @Test
    public void testMarkMessageAsRead() {
        // Part 1: Create a new message
        Message msg = new Message(1L, 2L, note, "Change read state test");
        msg = repository.save(msg); // Save msg and get the generated ID

        assertFalse(msg.isRead()); // Check that it's saved as unread first

        // Part 2: Call the function
        repository.markMessageAsRead(1L, 2L);

        // Part 3: Assert that it's changed
        Message updatedMsg = repository.findById(msg.getId()).get();
        assertTrue(updatedMsg.isRead());
    }

}
