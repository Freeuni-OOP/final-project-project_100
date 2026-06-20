package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.QuizAttempt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// force h2 to act like mysql and skip user foreign key checks
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;REFERENTIAL_INTEGRITY=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
class QuizAttemptRepositoryTest {

    @Autowired
    private QuizAttemptRepository repository;

    @Test
    void filtersPracticeAndOtherUsers() {
        QuizAttempt real = new QuizAttempt();
        real.setUserId(1L);
        real.setPractice(false); // target

        QuizAttempt practice = new QuizAttempt();
        practice.setUserId(1L);
        practice.setPractice(true); // ignore

        QuizAttempt otherUser = new QuizAttempt();
        otherUser.setUserId(99L);
        otherUser.setPractice(false); // ignore

        repository.save(real);
        repository.save(practice);
        repository.save(otherUser);

        List<QuizAttempt> history = repository.getUserHistory(1L);

        // should only fetch the 1 target
        assertEquals(1, history.size());
        assertFalse(history.get(0).isPractice());
        assertEquals(1L, history.get(0).getUserId());
    }

    @Test
    void returnsEmptyListIfNoAttempts() {
        List<QuizAttempt> history = repository.getUserHistory(999L); // ghost user

        assertEquals(0, history.size());
    }

    @Test
    void returnsMultipleAttempts() {
        QuizAttempt a1 = new QuizAttempt();
        a1.setUserId(1L);
        a1.setPractice(false);

        QuizAttempt a2 = new QuizAttempt();
        a2.setUserId(1L);
        a2.setPractice(false);

        repository.save(a1);
        repository.save(a2);

        List<QuizAttempt> history = repository.getUserHistory(1L);

        assertEquals(2, history.size());
    }
}