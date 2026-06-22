package com.freeuni.proj_100.quizwebsite;

import com.freeuni.proj_100.quizwebsite.model.AnswerEntity;
import com.freeuni.proj_100.quizwebsite.model.MultipleChoiceQuestion;
import com.freeuni.proj_100.quizwebsite.model.PictureResponseQuestion;
import com.freeuni.proj_100.quizwebsite.model.Question;
import com.freeuni.proj_100.quizwebsite.model.QuestionEntity;
import com.freeuni.proj_100.quizwebsite.model.StandardQuestion;
import com.freeuni.proj_100.quizwebsite.repository.AnswerRepository;
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
    private AnswerRepository answerRepository;
    private QuestionService questionService;

    @BeforeEach
    public void setUp() {
        questionRepository = mock(QuestionRepository.class);
        answerRepository = mock(AnswerRepository.class);
        questionService = new QuestionService(questionRepository, answerRepository);
    }

    @Test
    public void testGetQuestionsForQuizBuildsQuestionObjectsInOrder() {
        QuestionEntity q1 = questionEntity(1, 100, "STANDARD", "Who was first president?", null, 1);
        QuestionEntity q2 = questionEntity(2, 100, "MULTIPLE_CHOICE", "Pick the correct answer", null, 2);
        QuestionEntity q3 = questionEntity(3, 100, "PICTURE_RESPONSE", "Who is in the image?", "image.jpg", 3);

        when(questionRepository.findAllForQuiz(100)).thenReturn(List.of(q1, q2, q3));

        when(answerRepository.findCorrectForQuestion(1)).thenReturn(List.of(
                answer(1, "Washington", true, 0),
                answer(1, "George Washington", true, 0)
        ));

        when(answerRepository.findCorrectForQuestion(2)).thenReturn(List.of(
                answer(2, "A", true, 0)
        ));
        when(answerRepository.findAllForQuestion(2)).thenReturn(List.of(
                answer(2, "A", true, 0),
                answer(2, "B", false, 1),
                answer(2, "C", false, 2)
        ));

        when(answerRepository.findCorrectForQuestion(3)).thenReturn(List.of(
                answer(3, "Lincoln", true, 0)
        ));

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
        QuestionEntity q2 = questionEntity(2, 100, "STANDARD", "Question 2", null, 2);
        QuestionEntity q3 = questionEntity(3, 100, "STANDARD", "Question 3", null, 3);

        when(questionRepository.findAllForQuiz(100)).thenReturn(List.of(q1, q2, q3));
        when(answerRepository.findCorrectForQuestion(1)).thenReturn(List.of(answer(1, "Correct 1", true, 0)));
        when(answerRepository.findCorrectForQuestion(2)).thenReturn(List.of(answer(2, "Correct 2", true, 0)));
        when(answerRepository.findCorrectForQuestion(3)).thenReturn(List.of(answer(3, "Correct 3", true, 0)));

        Map<String, String[]> params = new HashMap<>();
        params.put("question_1", new String[]{"Correct 1"});
        params.put("question_2", new String[]{"wrong"});
        params.put("question_3", new String[]{"Correct 3"});

        assertEquals(2, questionService.scoreQuiz(100, params));
    }

    @Test
    public void testScoreQuizAcceptsCaseInsensitiveAndTrimmedAnswer() {
        QuestionEntity q1 = questionEntity(1, 100, "STANDARD", "Question", null, 1);

        when(questionRepository.findAllForQuiz(100)).thenReturn(List.of(q1));
        when(answerRepository.findCorrectForQuestion(1)).thenReturn(List.of(
                answer(1, "George Washington", true, 0)
        ));

        Map<String, String[]> params = new HashMap<>();
        params.put("question_1", new String[]{"  george washington  "});

        assertEquals(1, questionService.scoreQuiz(100, params));
    }

    @Test
    public void testScoreQuizReturnsZeroForMissingAnswers() {
        QuestionEntity q1 = questionEntity(1, 100, "STANDARD", "Question", null, 1);

        when(questionRepository.findAllForQuiz(100)).thenReturn(List.of(q1));
        when(answerRepository.findCorrectForQuestion(1)).thenReturn(List.of(
                answer(1, "Answer", true, 0)
        ));

        assertEquals(0, questionService.scoreQuiz(100, new HashMap<>()));
        assertEquals(0, questionService.scoreQuiz(100, null));
    }

    @Test
    public void testOnlyMultipleChoiceLoadsAllAnswersAsOptions() {
        QuestionEntity q1 = questionEntity(1, 100, "STANDARD", "Question 1", null, 1);
        QuestionEntity q2 = questionEntity(2, 100, "MULTIPLE_CHOICE", "Question 2", null, 2);

        when(questionRepository.findAllForQuiz(100)).thenReturn(List.of(q1, q2));
        when(answerRepository.findCorrectForQuestion(1)).thenReturn(List.of(answer(1, "Answer", true, 0)));
        when(answerRepository.findCorrectForQuestion(2)).thenReturn(List.of(answer(2, "A", true, 0)));
        when(answerRepository.findAllForQuestion(2)).thenReturn(List.of(
                answer(2, "A", true, 0),
                answer(2, "B", false, 1)
        ));

        questionService.getQuestionsForQuiz(100, false);

        verify(answerRepository, never()).findAllForQuestion(1);
        verify(answerRepository).findAllForQuestion(2);
    }

    private QuestionEntity questionEntity(int id, int quizId, String type,
                                          String prompt, String imageUrl, int sequenceNum) {
        QuestionEntity entity = new QuestionEntity();
        entity.setId(id);
        entity.setQuiz_id(quizId);
        entity.setQ_type(type);
        entity.setPrompt(prompt);
        entity.setImage_url(imageUrl);
        entity.setSequence_num(sequenceNum);
        return entity;
    }

    private AnswerEntity answer(int questionId, String text, boolean correct, int slotNum) {
        AnswerEntity answer = new AnswerEntity();
        answer.setQuestionId(questionId);
        answer.setAnswerText(text);
        answer.setCorrect(correct);
        answer.setSlotNum(slotNum);
        return answer;
    }
}