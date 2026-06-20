package com.freeuni.proj_100.quizwebsite;

import com.freeuni.proj_100.quizwebsite.model.StandardQuestion;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StandardQuestionTest {
    @Test
    public void testConstructorAndGetters(){
        StandardQuestion standardQuestion = new StandardQuestion(
                1,
                5,
                "Something",
                "STANDARD",
                List.of("123", "321")
        );

        assertEquals(1, standardQuestion.getQuestionID());
        assertEquals(5, standardQuestion.getQuizID());
        assertEquals("Something", standardQuestion.getQuestionPrompt());
        assertEquals("STANDARD", standardQuestion.getQuestionType());
    }

    @Test
    public void testCheckAnswer(){
        StandardQuestion standardQuestion = new StandardQuestion(
                1,
                5,
                "Something",
                "STANDARD",
                List.of("Something", "Something else")
        );
        Map<String, String[]> params = new HashMap<>();
        params.put("question_1", new String[]{"Something"});
        assertTrue(standardQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"Something else"});
        assertTrue(standardQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"someThInG ELSE"});
        assertTrue(standardQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"Not something or something else"});
        assertFalse(standardQuestion.checkAnswer(params));

        params = null;
        assertFalse(standardQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_123", new String[]{"Something"});
        assertFalse(standardQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", null);
        assertFalse(standardQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{});
        assertFalse(standardQuestion.checkAnswer(params));
    }
}
