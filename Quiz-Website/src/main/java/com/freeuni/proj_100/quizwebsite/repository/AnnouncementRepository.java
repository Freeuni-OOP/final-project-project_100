package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository interface for {@link Announcement} entities.
 * <p>
 * Extends {@link JpaRepository} to inherit standard CRUD capability, pagination, 
 * and sorting operations managed by Spring Data JPA.
 * </p>
 */
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    /**
     * Retrieves all announcements from the database ordered chronologically.
     *
     * @return a {@link List} of {@link Announcement} entities, ordered by creation time descending
     */
    List<Announcement> findAllByOrderByCreatedAtDesc();
}
