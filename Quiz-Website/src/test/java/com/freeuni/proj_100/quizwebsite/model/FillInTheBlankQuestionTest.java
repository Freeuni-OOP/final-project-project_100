package com.freeuni.proj_100.quizwebsite.model;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FillInTheBlankQuestionTest {
    @Test
    public void testConstructorAndGetters(){
        FillInTheBlankQuestion fillInTheBlankQuestion = new FillInTheBlankQuestion(
                1,
                5,
                "Something",
                "FILL_IN_THE_BLANK",
                List.of("123", "321")
        );

        assertEquals(1, fillInTheBlankQuestion.getQuestionID());
        assertEquals(5, fillInTheBlankQuestion.getQuizID());
        assertEquals("Something", fillInTheBlankQuestion.getQuestionPrompt());
        assertEquals("FILL_IN_THE_BLANK", fillInTheBlankQuestion.getQuestionType());
    }

    @Test
    public void testCheckAnswer(){
        FillInTheBlankQuestion fillInTheBlankQuestion = new FillInTheBlankQuestion(
                1,
                5,
                "Something",
                "FILL_IN_THE_BLANK",
                List.of("Something", "Something else")
        );
        Map<String, String[]> params = new HashMap<>();
        params.put("question_1", new String[]{"Something"});
        assertTrue(fillInTheBlankQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"Something else"});
        assertTrue(fillInTheBlankQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"someThInG ELSE"});
        assertTrue(fillInTheBlankQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"Not something or something else"});
        assertFalse(fillInTheBlankQuestion.checkAnswer(params));

        params = null;
        assertFalse(fillInTheBlankQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_123", new String[]{"Something"});
        assertFalse(fillInTheBlankQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", null);
        assertFalse(fillInTheBlankQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{});
        assertFalse(fillInTheBlankQuestion.checkAnswer(params));
    }
}
