package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.UserAchievement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// force h2 to act like mysql and skip user foreign key checks
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;REFERENTIAL_INTEGRITY=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
class UserAchievementRepositoryTest {

    @Autowired
    private UserAchievementRepository repository;

    @Test
    void findsAchievementsByUserId() {
        UserAchievement target1 = new UserAchievement();
        target1.setUserId(1L);
        target1.setAchievementType("amateur_author");

        UserAchievement target2 = new UserAchievement();
        target2.setUserId(1L);
        target2.setAchievementType("quiz_machine");

        UserAchievement otherUser = new UserAchievement();
        otherUser.setUserId(99L); // ignore
        otherUser.setAchievementType("amateur_author");

        repository.save(target1);
        repository.save(target2);
        repository.save(otherUser);

        List<UserAchievement> achievements = repository.findByUserId(1L);

        // should only fetch the 2 targets for user 1
        assertEquals(2, achievements.size());
        assertEquals(1L, achievements.get(0).getUserId());
    }

    @Test
    void returnsEmptyListIfNoAchievements() {
        List<UserAchievement> achievements = repository.findByUserId(999L); // ghost user

        assertEquals(0, achievements.size());
    }
}