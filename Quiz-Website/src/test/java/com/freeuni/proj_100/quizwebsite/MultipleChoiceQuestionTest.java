package com.freeuni.proj_100.quizwebsite;

import com.freeuni.proj_100.quizwebsite.model.MultipleChoiceQuestion;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MultipleChoiceQuestionTest {
    @Test
    public void testConstructorAndGetters(){
        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(
                1,
                5,
                "Something",
                "MULTIPLE_CHOICE",
                List.of("123"),
                List.of("123", "321", "231")
        );

        assertEquals(List.of("123", "321", "231"), multipleChoiceQuestion.getOptions());
        assertEquals(1, multipleChoiceQuestion.getQuestionID());
        assertEquals(5, multipleChoiceQuestion.getQuizID());
        assertEquals("Something", multipleChoiceQuestion.getQuestionPrompt());
        assertEquals("MULTIPLE_CHOICE", multipleChoiceQuestion.getQuestionType());
    }

    @Test
    public void testCheckAnswer(){
        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(
                1,
                5,
                "Something",
                "MULTIPLE_CHOICE",
                List.of("Something", "Something else"),
                List.of("123", "321", "231")
        );
        Map<String, String[]> params = new HashMap<>();
        params.put("question_1", new String[]{"Something"});
        assertTrue(multipleChoiceQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"Something else"});
        assertTrue(multipleChoiceQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"someThInG ELSE"});
        assertTrue(multipleChoiceQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"Not something or something else"});
        assertFalse(multipleChoiceQuestion.checkAnswer(params));

        params = null;
        assertFalse(multipleChoiceQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_123", new String[]{"Something"});
        assertFalse(multipleChoiceQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", null);
        assertFalse(multipleChoiceQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{});
        assertFalse(multipleChoiceQuestion.checkAnswer(params));
    }
}
