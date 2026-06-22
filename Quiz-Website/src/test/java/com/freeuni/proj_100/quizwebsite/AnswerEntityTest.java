package com.freeuni.proj_100.quizwebsite;

import com.freeuni.proj_100.quizwebsite.model.AnswerEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnswerEntityTest {

    @Test
    public void testSettersAndGetters() {
        AnswerEntity answer = new AnswerEntity();

        answer.setId(1);
        answer.setQuestionId(10);
        answer.setAnswerText("George Washington");
        answer.setCorrect(true);
        answer.setSlotNum(2);

        assertEquals(1, answer.getId());
        assertEquals(10, answer.getQuestionId());
        assertEquals("George Washington", answer.getAnswerText());
        assertTrue(answer.isCorrect());
        assertEquals(2, answer.getSlotNum());
    }

    @Test
    public void testDefaultValues() {
        AnswerEntity answer = new AnswerEntity();

        assertEquals(0, answer.getId());
        assertEquals(0, answer.getQuestionId());
        assertNull(answer.getAnswerText());
        assertFalse(answer.isCorrect());
        assertEquals(0, answer.getSlotNum());
    }
}