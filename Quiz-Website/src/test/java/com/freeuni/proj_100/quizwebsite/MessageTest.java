package com.freeuni.proj_100.quizwebsite;

import com.freeuni.proj_100.quizwebsite.model.Message;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static com.freeuni.proj_100.quizwebsite.model.Message.MessageType.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the Message entity — constructor, getters, and setters.
 */
public class MessageTest {

    @Test
    public void testConstructorAndGetters() {
        Message msg = new Message(2, 3, note, "Mr Epstein");

        assertEquals(2, msg.getSenderId());
        assertEquals(3, msg.getReceiverId());
        assertEquals(note, msg.getType());
        assertEquals("Mr Epstein", msg.getTextContent());
        assertFalse(msg.isRead());

        // These are null for now, because they are not yet generated
        assertNull(msg.getId());
        assertNull(msg.getSentAt());
    }

    @Test
    public void testSetters() {
        Message msg = new Message();

        msg.setId(9);
        assertEquals(9, msg.getId());

        msg.setSenderId(3);
        assertEquals(3, msg.getSenderId());

        msg.setReceiverId(4);
        assertEquals(4, msg.getReceiverId());

        msg.setType(challenge);
        assertEquals(challenge, msg.getType());

        msg.setTextContent("Kim Jong Un");
        assertEquals("Kim Jong Un", msg.getTextContent());

        msg.setRead(true);
        assertTrue(msg.isRead());

        msg.setSentAt(Timestamp.valueOf("2026-06-27 11:56:00.123"));
        assertEquals(Timestamp.valueOf("2026-06-27 11:56:00.123"), msg.getSentAt());
    }
}