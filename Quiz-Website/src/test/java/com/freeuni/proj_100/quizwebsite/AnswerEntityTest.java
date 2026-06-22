package com.freeuni.proj_100.quizwebsite;

import com.freeuni.proj_100.quizwebsite.model.AnswerEntity;
import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AnswerEntity field accessors.
 */
public class AnswerEntityTest {
    /**
     * Verifies that all AnswerEntity setters and getters preserve values.
     */
    @Test
    public void testSettersAndGetters() {
        QuestionEntity question = new QuestionEntity();
        AnswerEntity answer = new AnswerEntity();

        answer.setId(1);
        answer.setQuestionId(10);
        answer.setQuestion(question);
        answer.setAnswerText("George Washington");
        answer.setCorrect(true);
        answer.setSlotNum(2);

        assertEquals(1, answer.getId());
        assertEquals(10, answer.getQuestionId());
        assertSame(question, answer.getQuestion());
        assertEquals("George Washington", answer.getAnswerText());
        assertTrue(answer.isCorrect());
        assertEquals(2, answer.getSlotNum());
    }

    /**
     * Verifies default values on a newly constructed AnswerEntity.
     */
    @Test
    public void testDefaultValues() {
        AnswerEntity answer = new AnswerEntity();

        assertEquals(0, answer.getId());
        assertEquals(0, answer.getQuestionId());
        assertNull(answer.getQuestion());
        assertNull(answer.getAnswerText());
        assertFalse(answer.isCorrect());
        assertEquals(0, answer.getSlotNum());
    }
}