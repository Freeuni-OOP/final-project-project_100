package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.User;
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
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;REFERENTIAL_INTEGRITY=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
class UserAchievementRepositoryTest {

    @Autowired
    private UserAchievementRepository repository;

    @Test
    void testFindsAchievementsForSpecificUserOnly() {
        User target = new User(); target.setId(1L);
        User other = new User(); other.setId(99L);

        UserAchievement a1 = new UserAchievement();
        a1.setUser(target); a1.setAchievementType("amateur_author");

        UserAchievement a2 = new UserAchievement();
        a2.setUser(target); a2.setAchievementType("quiz_machine");

        UserAchievement a3 = new UserAchievement();
        a3.setUser(other); a3.setAchievementType("amateur_author");

        repository.save(a1);
        repository.save(a2);
        repository.save(a3);

        List<UserAchievement> achievements = repository.findByUser_Id(1L);

        assertEquals(2, achievements.size());
        assertEquals(1L, achievements.get(0).getUser().getId());
    }

    @Test
    void testReturnsEmptyListIfNoAchievements() {
        List<UserAchievement> achievements = repository.findByUser_Id(999L);
        assertEquals(0, achievements.size());
    }
}