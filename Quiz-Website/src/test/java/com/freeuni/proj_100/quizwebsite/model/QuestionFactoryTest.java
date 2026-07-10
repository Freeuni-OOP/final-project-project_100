package com.freeuni.proj_100.quizwebsite;

import com.freeuni.proj_100.quizwebsite.model.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionFactoryTest {
    private QuestionEntity entity;
    private List<String> answers;
    private List<String> options;

    @BeforeEach
    public void setUp(){
        Quiz quiz = new Quiz();
        quiz.setId(1);

        entity = new QuestionEntity();
        entity.setId(1);
        entity.setQuiz(quiz);
        entity.setPrompt("Some prompt");
        entity.setImageUrl("Some image url");

        answers = List.of("Flynn Taggart");
        options = List.of("Flynn Taggart", "Kratos Of Sparta", "Alexios Deimos");
    }

    @Test
    public void testStandardQuestionCreation(){
        entity.setQType("STANDARD");
        Question question = QuestionFactory.createQuestion(entity, answers, options);

        assertInstanceOf(StandardQuestion.class, question);
        assertEquals(1, question.getQuestionID());
        assertEquals(1, question.getQuizID());
        assertEquals("Some prompt", question.getQuestionPrompt());
        assertEquals("STANDARD", question.getQuestionType());
    }

    @Test
    public void testFillInTheBlankQuestionCreation(){
        entity.setQType("FILL_IN_THE_BLANK");
        Question question = QuestionFactory.createQuestion(entity, answers, options);

        assertInstanceOf(FillInTheBlankQuestion.class, question);
        assertEquals(1, question.getQuestionID());
        assertEquals(1, question.getQuizID());
        assertEquals("Some prompt", question.getQuestionPrompt());
        assertEquals("FILL_IN_THE_BLANK", question.getQuestionType());
    }

    @Test
    public void testMultipleChoiceQuestionCreation(){
        entity.setQType("MULTIPLE_CHOICE");
        Question question = QuestionFactory.createQuestion(entity, answers, options);

        assertInstanceOf(MultipleChoiceQuestion.class, question);
        assertEquals(1, question.getQuestionID());
        assertEquals(1, question.getQuizID());
        assertEquals("Some prompt", question.getQuestionPrompt());
        assertEquals("MULTIPLE_CHOICE", question.getQuestionType());
    }

    @Test
    public void testPictureResponseQuestionCreation(){
        entity.setQType("PICTURE_RESPONSE");
        Question question = QuestionFactory.createQuestion(entity, answers, options);

        assertInstanceOf(PictureResponseQuestion.class, question);
        assertEquals(1, question.getQuestionID());
        assertEquals(1, question.getQuizID());
        assertEquals("Some prompt", question.getQuestionPrompt());
        assertEquals("Some image url", entity.getImageUrl());
        assertEquals("PICTURE_RESPONSE", question.getQuestionType());
    }

    @Test
    public void testNullOrEmptyQuestionType(){
        entity.setQType("");

        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, ()-> {
            QuestionFactory.createQuestion(entity, answers, options);
        });

        assertEquals("Question type can't be null or empty.", illegalArgumentException.getMessage());

        entity.setQType(null);

        illegalArgumentException =
                assertThrows(IllegalArgumentException.class, ()-> {
                    QuestionFactory.createQuestion(entity, answers, options);
                });

        assertEquals("Question type can't be null or empty.", illegalArgumentException.getMessage());
    }

    @Test
    public void testUnknownQuestionType(){
        entity.setQType("Some Other Type Of Question");

        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, ()-> {
                    QuestionFactory.createQuestion(entity, answers, options);
                });

        assertEquals("Unknown question type: Some Other Type Of Question", illegalArgumentException.getMessage());
    }
}