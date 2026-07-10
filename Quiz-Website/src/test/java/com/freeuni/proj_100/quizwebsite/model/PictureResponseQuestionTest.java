package com.freeuni.proj_100.quizwebsite.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PictureResponseQuestionTest {
    @Test
    public void testConstructorAndGetters(){
        PictureResponseQuestion pictureResponseQuestion = new PictureResponseQuestion(
                1,
                5,
                "Something",
                "PICTURE_RESPONSE",
                "Image url",
                List.of("123", "321", "231")
        );

        assertEquals("Image url", pictureResponseQuestion.getImageUrl());
        assertEquals(1, pictureResponseQuestion.getQuestionID());
        assertEquals(5, pictureResponseQuestion.getQuizID());
        assertEquals("Something", pictureResponseQuestion.getQuestionPrompt());
        assertEquals("PICTURE_RESPONSE", pictureResponseQuestion.getQuestionType());
    }

    @Test
    public void testCheckAnswer(){
        PictureResponseQuestion pictureResponseQuestion = new PictureResponseQuestion(
                1,
                5,
                "Something",
                "MULTIPLE_CHOICE",
                "Image url",
                List.of("Something", "Something else")
        );
        Map<String, String[]> params = new HashMap<>();
        params.put("question_1", new String[]{"Something"});
        assertTrue(pictureResponseQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"Something else"});
        assertTrue(pictureResponseQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"someThInG ELSE"});
        assertTrue(pictureResponseQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{"Not something or something else"});
        assertFalse(pictureResponseQuestion.checkAnswer(params));

        params = null;
        assertFalse(pictureResponseQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_123", new String[]{"Something"});
        assertFalse(pictureResponseQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", null);
        assertFalse(pictureResponseQuestion.checkAnswer(params));

        params = new HashMap<>();
        params.put("question_1", new String[]{});
        assertFalse(pictureResponseQuestion.checkAnswer(params));
    }
}
