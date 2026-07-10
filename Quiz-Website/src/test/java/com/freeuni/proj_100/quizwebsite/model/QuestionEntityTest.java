package com.freeuni.proj_100.quizwebsite.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionEntityTest {
    
    @Test
    public void testSettersAndGetters(){
        Quiz quiz = new Quiz();
        quiz.setId(5);

        QuestionEntity entity = new QuestionEntity();
        entity.setId(1);
        
        entity.setQuiz(quiz);
        
        entity.setQType("STANDARD");
        entity.setPrompt("Something");
        entity.setImageUrl("Some image url");
        entity.setSequenceNum(3);

        assertEquals(1, entity.getId());
        assertSame(quiz, entity.getQuiz());
        assertEquals(5, entity.getQuiz().getId());
        assertEquals("STANDARD", entity.getQType());
        assertEquals("Something", entity.getPrompt());
        assertEquals("Some image url", entity.getImageUrl());
        assertEquals(3, entity.getSequenceNum());
    }

    @Test
    public void testNullField(){
        Quiz quiz = new Quiz();
        quiz.setId(5);

        QuestionEntity entity = new QuestionEntity();
        entity.setId(1);
        entity.setQuiz(quiz);
        entity.setQType("STANDARD");
        entity.setPrompt("Something");
        entity.setImageUrl(null);
        entity.setSequenceNum(2);

        assertNull(entity.getImageUrl());
        assertEquals(1, entity.getId());
        assertEquals(5, entity.getQuiz().getId());
    }
}