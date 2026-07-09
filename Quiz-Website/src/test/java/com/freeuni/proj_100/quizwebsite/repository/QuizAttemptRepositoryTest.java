package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import com.freeuni.proj_100.quizwebsite.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;REFERENTIAL_INTEGRITY=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
class QuizAttemptRepositoryTest {

    @Autowired
    private QuizAttemptRepository repository;

    @Test
    void testFiltersPracticeAndOtherUsers() {
        User target = new User(); target.setId(1);
        User other = new User(); other.setId(99);

        QuizAttempt real = new QuizAttempt();
        real.setUser(target); real.setQuizId(10); real.setPractice(false);

        QuizAttempt practice = new QuizAttempt();
        practice.setUser(target); practice.setQuizId(10); practice.setPractice(true);

        QuizAttempt otherUserAttempt = new QuizAttempt();
        otherUserAttempt.setUser(other); otherUserAttempt.setQuizId(10); otherUserAttempt.setPractice(false);

        repository.save(real);
        repository.save(practice);
        repository.save(otherUserAttempt);

        List<QuizAttempt> history = repository.findByUserIdAndIsPracticeFalseOrderByTakenAtDesc(1);

        assertEquals(1, history.size());
        assertFalse(history.get(0).isPractice());
        assertEquals(1, history.get(0).getUser().getId());
    }

    @Test
    void testReturnsEmptyListIfNoAttempts() {
        List<QuizAttempt> history = repository.findByUserIdAndIsPracticeFalseOrderByTakenAtDesc(999);
        assertEquals(0, history.size());
    }

    @Test
    void testOrdersHistoryByMostRecent() {
        User player = new User(); player.setId(1);

        QuizAttempt oldAttempt = new QuizAttempt();
        oldAttempt.setUser(player); oldAttempt.setQuizId(5); oldAttempt.setPractice(false);
        oldAttempt.setTakenAt(LocalDateTime.now().minusDays(2));

        QuizAttempt newAttempt = new QuizAttempt();
        newAttempt.setUser(player); newAttempt.setQuizId(5); newAttempt.setPractice(false);
        newAttempt.setTakenAt(LocalDateTime.now());

        repository.save(oldAttempt);
        repository.save(newAttempt);

        List<QuizAttempt> history = repository.findByUserIdAndIsPracticeFalseOrderByTakenAtDesc(1);

        assertEquals(2, history.size());
        // The newest one should be first in the list
        assertTrue(history.get(0).getTakenAt().isAfter(history.get(1).getTakenAt()));
    }

    @Test
    void testAppliesLeaderboardPaginationAndFilters() {
        User player = new User(); player.setId(1);

        QuizAttempt highscore = new QuizAttempt();
        highscore.setUser(player);
        highscore.setQuizId(5);
        highscore.setScore(100);
        highscore.setPractice(false);

        QuizAttempt practiceScore = new QuizAttempt();
        practiceScore.setUser(player);
        practiceScore.setQuizId(5);
        practiceScore.setScore(200);
        practiceScore.setPractice(true);

        repository.save(highscore);
        repository.save(practiceScore);

        List<QuizAttempt> leaderboard =
                repository.findByQuizIdAndIsPracticeFalseOrderByScoreDescTimeTakenSecDesc(5, PageRequest.of(0, 10));

        assertEquals(1, leaderboard.size());
        assertEquals(100, leaderboard.get(0).getScore());
    }
}