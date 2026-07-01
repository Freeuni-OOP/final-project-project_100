package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import com.freeuni.proj_100.quizwebsite.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;REFERENTIAL_INTEGRITY=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.generate-ddl=true",
        "spring.sql.init.mode=never",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class QuizAttemptRepositoryTest {

    @Autowired
    private QuizAttemptRepository repository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        userRepository.deleteAll();
    }

    private User createAndSaveTestUser(String username) {
        User user = new User(
                null,
                username,
                username + "@example.com",
                "mock_hash",
                LocalDateTime.now(),
                false
        );
        return userRepository.save(user);
    }

    @Test
    void testGetTopPerformersAllTime() {
        User player = createAndSaveTestUser("player1");
        LocalDateTime baseTime = LocalDateTime.of(2026, 7, 1, 12, 0);

        QuizAttempt attempt1 = new QuizAttempt();
        attempt1.setUser(player); attempt1.setQuizId(5L); attempt1.setPractice(false);
        attempt1.setScore(100); attempt1.setTimeTakenSec(60);
        attempt1.setTakenAt(baseTime);

        QuizAttempt attempt2 = new QuizAttempt();
        attempt2.setUser(player); attempt2.setQuizId(5L); attempt2.setPractice(false);
        attempt2.setScore(100); attempt2.setTimeTakenSec(45);
        attempt2.setTakenAt(baseTime.plusMinutes(1)); // Separated to guarantee stable sorting context

        QuizAttempt practice = new QuizAttempt();
        practice.setUser(player); practice.setQuizId(5L); practice.setPractice(true);
        practice.setScore(200); practice.setTimeTakenSec(10);
        practice.setTakenAt(baseTime.plusMinutes(2));

        repository.saveAll(List.of(attempt1, attempt2, practice));

        List<QuizAttempt> top = repository.getTopPerformersAllTime(5L, PageRequest.of(0, 10));

<<<<<<< Updated upstream
        List<QuizAttempt> history = repository.findByUserIdAndIsPracticeFalseOrderByTakenAtDesc(1L);

        assertEquals(1, history.size());
        assertFalse(history.get(0).isPractice());
        assertEquals(1L, history.get(0).getUser().getId());
    }

    @Test
    void testReturnsEmptyListIfNoAttempts() {
        List<QuizAttempt> history = repository.findByUserIdAndIsPracticeFalseOrderByTakenAtDesc(999L);
        assertEquals(0, history.size());
    }

    @Test
    void testOrdersHistoryByMostRecent() {
        User player = new User(); player.setId(1L);
=======
        assertEquals(2, top.size());
        assertEquals(45, top.get(0).getTimeTakenSec()); // Verifies tiebreaker sort logic (timeTakenSec ASC)
    }

    @Test
    void testGetTopPerformersLastDay() {
        User player = createAndSaveTestUser("player2");
        // Frozen base reference point to avoid clock drift issues completely
        LocalDateTime frozenNow = LocalDateTime.of(2026, 7, 1, 12, 0);
>>>>>>> Stashed changes

        QuizAttempt oldAttempt = new QuizAttempt();
        oldAttempt.setUser(player); oldAttempt.setQuizId(5L); oldAttempt.setPractice(false);
        oldAttempt.setTakenAt(frozenNow.minusDays(2));
        oldAttempt.setScore(80); oldAttempt.setTimeTakenSec(50);

        QuizAttempt newAttempt = new QuizAttempt();
        newAttempt.setUser(player); newAttempt.setQuizId(5L); newAttempt.setPractice(false);
        newAttempt.setTakenAt(frozenNow.minusHours(1));
        newAttempt.setScore(90); newAttempt.setTimeTakenSec(40);

        repository.saveAll(List.of(oldAttempt, newAttempt));

<<<<<<< Updated upstream
        List<QuizAttempt> history = repository.findByUserIdAndIsPracticeFalseOrderByTakenAtDesc(1L);
=======
        List<QuizAttempt> recentTop = repository.getTopPerformersLastDay(5L, frozenNow.minusDays(1), PageRequest.of(0, 10));
>>>>>>> Stashed changes

        assertEquals(1, recentTop.size());
        assertEquals(newAttempt.getTakenAt(), recentTop.get(0).getTakenAt());
    }

    @Test
    void testGetRecentTestTakers() {
        User player = createAndSaveTestUser("player3");
        LocalDateTime baseTime = LocalDateTime.of(2026, 7, 1, 12, 0);

<<<<<<< Updated upstream
        QuizAttempt highscore = new QuizAttempt();
        highscore.setUser(player);
        highscore.setQuizId(5L);
        highscore.setScore(100);
        highscore.setPractice(false);

        QuizAttempt practiceScore = new QuizAttempt();
        practiceScore.setUser(player);
        practiceScore.setQuizId(5L);
        practiceScore.setScore(200);
        practiceScore.setPractice(true);
=======
        QuizAttempt older = new QuizAttempt();
        older.setUser(player); older.setQuizId(5L); older.setPractice(false);
        older.setTakenAt(baseTime.minusHours(5));
        older.setScore(70); older.setTimeTakenSec(30);

        QuizAttempt newer = new QuizAttempt();
        newer.setUser(player); newer.setQuizId(5L); newer.setPractice(false);
        newer.setTakenAt(baseTime); // Hardcoded sequential order safely avoids millisecond dropping drops
        newer.setScore(85); newer.setTimeTakenSec(25);
>>>>>>> Stashed changes

        repository.saveAll(List.of(older, newer));

<<<<<<< Updated upstream
        List<QuizAttempt> leaderboard =
                repository.findByQuizIdAndIsPracticeFalseOrderByScoreDescTimeTakenSecDesc(5L, PageRequest.of(0, 10));
=======
        List<QuizAttempt> recent = repository.getRecentTestTakers(5L, PageRequest.of(0, 10));
>>>>>>> Stashed changes

        assertEquals(2, recent.size());
        assertTrue(recent.get(0).getTakenAt().isAfter(recent.get(1).getTakenAt()));
    }

    @Test
    void testGetUserPastPerformance() {
        User player1 = createAndSaveTestUser("tazo");
        User player2 = createAndSaveTestUser("other");
        LocalDateTime baseTime = LocalDateTime.of(2026, 7, 1, 12, 0);

        QuizAttempt attemptTazo = new QuizAttempt();
        attemptTazo.setUser(player1); attemptTazo.setQuizId(5L); attemptTazo.setPractice(false);
        attemptTazo.setTakenAt(baseTime);
        attemptTazo.setScore(95); attemptTazo.setTimeTakenSec(35);

        QuizAttempt attemptOther = new QuizAttempt();
        attemptOther.setUser(player2); attemptOther.setQuizId(5L); attemptOther.setPractice(false);
        attemptOther.setTakenAt(baseTime.plusMinutes(1));
        attemptOther.setScore(60); attemptOther.setTimeTakenSec(40);

        repository.saveAll(List.of(attemptTazo, attemptOther));

        List<QuizAttempt> history = repository.getUserPastPerformance(5L, "tazo");

        assertEquals(1, history.size());
        assertEquals("tazo", history.get(0).getUser().getUsername());
    }
}