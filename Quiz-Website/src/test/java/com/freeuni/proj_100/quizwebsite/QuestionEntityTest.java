package com.freeuni.proj_100.quizwebsite;

import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionEntityTest {
    @Test
    public void testSettersAndGetters(){
        QuestionEntity entity = new QuestionEntity();
        entity.setId(1);
        entity.setQuiz_id(5);
        entity.setQ_type("STANDARD");
        entity.setPrompt("Something");
        entity.setImage_url("Some image url");
        entity.setSequence_num(3);

        assertEquals(1, entity.getId());
        assertEquals(5, entity.getQuiz_id());
        assertEquals("STANDARD", entity.getQ_type());
        assertEquals("Something", entity.getPrompt());
        assertEquals("Some image url", entity.getImage_url());
        assertEquals(3, entity.getSequence_num());
    }

    @Test
    public void testNullField(){
        QuestionEntity entity = new QuestionEntity();
        entity.setId(1);
        entity.setQuiz_id(5);
        entity.setQ_type("STANDARD");
        entity.setPrompt("Something");
        entity.setImage_url(null);
        entity.setSequence_num(2);

        assertNull(entity.getImage_url());
        assertEquals(1, entity.getId());
        assertEquals(5, entity.getQuiz_id());
    }
}
