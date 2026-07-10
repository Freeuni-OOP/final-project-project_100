package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.model.*;
import com.freeuni.proj_100.quizwebsite.repository.FriendshipRepository;
import com.freeuni.proj_100.quizwebsite.repository.QuestionRepository;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceTest {

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendshipService friendshipService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(); user1.setId(1); user1.setUsername("tazo");
        user2 = new User(); user2.setId(2); user2.setUsername("john");
    }

    @Test
    void testSendFriendRequestSuccessfully() {
        when(userRepository.findByUsername("tazo")).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user2));
        when(friendshipRepository.findFriendshipBetween(1, 2)).thenReturn(Optional.empty());

        friendshipService.sendFriendRequest("tazo", "john");

        verify(friendshipRepository, times(1)).save(any(Friendship.class));
    }

    @Test
    void testSendFriendRequestFailsIfSelf() {
        when(userRepository.findByUsername("tazo")).thenReturn(Optional.of(user1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            friendshipService.sendFriendRequest("tazo", "tazo");
        });

        assertEquals("You cannot send a friend request to yourself.", exception.getMessage());
    }

    @Test
    void testSendFriendRequestFailsIfBlockedByRejection() {
        when(userRepository.findByUsername("tazo")).thenReturn(Optional.of(user1)); // Sender
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user2)); // Target

        // Simulate that user1 previously sent a request, and user2 rejected it
        Friendship rejectedContext = new Friendship(1, 2, FriendshipStatus.REJECTED);
        when(friendshipRepository.findFriendshipBetween(1, 2)).thenReturn(Optional.of(rejectedContext));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            friendshipService.sendFriendRequest("tazo", "john");
        });

        assertEquals("You cannot send a friend request to this user.", exception.getMessage());
    }

    @Test
    void testAcceptFriendRequestFailsIfSenderTriesToAccept() {
        when(userRepository.findByUsername("tazo")).thenReturn(Optional.of(user1)); // Current user

        // Simulate a request where user1 (tazo) is the sender (userId = 1)
        Friendship pendingRequest = new Friendship(1, 2, FriendshipStatus.PENDING);
        when(friendshipRepository.findFriendshipBetween(2, 1)).thenReturn(Optional.of(pendingRequest));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            friendshipService.acceptFriendRequest("tazo", 2);
        });

        assertEquals("Unauthorized relationship manipulation.", exception.getMessage());
    }

    public static class QuestionServiceTest {
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
}