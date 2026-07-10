package com.freeuni.proj_100.quizwebsite;

import com.freeuni.proj_100.quizwebsite.model.AnswerEntity;
import com.freeuni.proj_100.quizwebsite.model.MultipleChoiceQuestion;
import com.freeuni.proj_100.quizwebsite.model.PictureResponseQuestion;
import com.freeuni.proj_100.quizwebsite.model.Question;
import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import com.freeuni.proj_100.quizwebsite.model.Quiz;
import com.freeuni.proj_100.quizwebsite.model.StandardQuestion;
import com.freeuni.proj_100.quizwebsite.repository.QuestionRepository;
import com.freeuni.proj_100.quizwebsite.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuestionServiceTest {
    private QuestionRepository questionRepository;
    private QuestionService questionService;

    @BeforeEach
    public void setUp() {
        questionRepository = mock(QuestionRepository.class);
        questionService = new QuestionService(questionRepository);
    }

    @Test
    public void testGetQuestionsForQuizBuildsQuestionObjectsInOrder() {
        QuestionEntity q1 = questionEntity(1, 100, "STANDARD", "Who was first president?", null, 1);
        q1.setAnswers(List.of(
                answer(q1, "Washington", true, 0),
                answer(q1, "George Washington", true, 1)
        ));

        QuestionEntity q2 = questionEntity(2, 100, "MULTIPLE_CHOICE", "Pick the correct answer", null, 2);
        q2.setAnswers(List.of(
                answer(q2, "A", true, 0),
                answer(q2, "B", false, 1),
                answer(q2, "C", false, 2)
        ));

        QuestionEntity q3 = questionEntity(3, 100, "PICTURE_RESPONSE", "Who is in the image?", "image.jpg", 3);
        q3.setAnswers(List.of(answer(q3, "Lincoln", true, 0)));

        when(questionRepository.findByQuizIdOrderBySequenceNumAscIdAsc(100))
                .thenReturn(List.of(q1, q2, q3));

        List<Question> questions = questionService.getQuestionsForQuiz(100, false);

        assertEquals(3, questions.size());
        assertInstanceOf(StandardQuestion.class, questions.get(0));
        assertEquals(1, questions.get(0).getQuestionID());

        assertInstanceOf(MultipleChoiceQuestion.class, questions.get(1));
        MultipleChoiceQuestion multipleChoice = (MultipleChoiceQuestion) questions.get(1);
        assertEquals(List.of("A", "B", "C"), multipleChoice.getOptions());

        assertInstanceOf(PictureResponseQuestion.class, questions.get(2));
        PictureResponseQuestion pictureResponse = (PictureResponseQuestion) questions.get(2);
        assertEquals("image.jpg", pictureResponse.getImageUrl());
    }

    @Test
    public void testScoreQuizCountsCorrectAnswers() {
        QuestionEntity q1 = questionEntity(1, 100, "STANDARD", "Question 1", null, 1);
        q1.setAnswers(List.of(answer(q1, "Correct 1", true, 0)));

        QuestionEntity q2 = questionEntity(2, 100, "STANDARD", "Question 2", null, 2);
        q2.setAnswers(List.of(answer(q2, "Correct 2", true, 0)));

        QuestionEntity q3 = questionEntity(3, 100, "STANDARD", "Question 3", null, 3);
        q3.setAnswers(List.of(answer(q3, "Correct 3", true, 0)));

        when(questionRepository.findByQuizIdOrderBySequenceNumAscIdAsc(100))
                .thenReturn(List.of(q1, q2, q3));

        Map<String, String[]> params = new HashMap<>();
        params.put("question_1", new String[]{"Correct 1"});
        params.put("question_2", new String[]{"wrong"});
        params.put("question_3", new String[]{"Correct 3"});

        assertEquals(2, questionService.scoreQuiz(100, params));
    }

    @Test
    public void testScoreQuizAcceptsCaseInsensitiveAndTrimmedAnswer() {
        QuestionEntity q1 = questionEntity(1, 100, "STANDARD", "Question", null, 1);
        q1.setAnswers(List.of(answer(q1, "George Washington", true, 0)));

        when(questionRepository.findByQuizIdOrderBySequenceNumAscIdAsc(100))
                .thenReturn(List.of(q1));

        Map<String, String[]> params = new HashMap<>();
        params.put("question_1", new String[]{"  george washington  "});

        assertEquals(1, questionService.scoreQuiz(100, params));
    }

    @Test
    public void testScoreQuizReturnsZeroForMissingAnswers() {
        QuestionEntity q1 = questionEntity(1, 100, "STANDARD", "Question", null, 1);
        q1.setAnswers(List.of(answer(q1, "Answer", true, 0)));

        when(questionRepository.findByQuizIdOrderBySequenceNumAscIdAsc(100))
                .thenReturn(List.of(q1));

        assertEquals(0, questionService.scoreQuiz(100, new HashMap<>()));
        assertEquals(0, questionService.scoreQuiz(100, null));
    }

    private QuestionEntity questionEntity(int id, int quizId, String type,
                                          String prompt, String imageUrl, int sequenceNum) {
        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        QuestionEntity entity = new QuestionEntity();
        entity.setId(id);
        entity.setQuiz(quiz);
        entity.setQType(type);
        entity.setPrompt(prompt);
        entity.setImageUrl(imageUrl);
        entity.setSequenceNum(sequenceNum);
        return entity;
    }

    private AnswerEntity answer(QuestionEntity question, String text, boolean correct, int slotNum) {
        AnswerEntity answer = new AnswerEntity();
        answer.setQuestion(question);
        answer.setAnswerText(text);
        answer.setCorrect(correct);
        answer.setSlotNum(slotNum);
        return answer;
    }
}