package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.AnnouncementDto;
import com.freeuni.proj_100.quizwebsite.model.Announcement;
import com.freeuni.proj_100.quizwebsite.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing platform announcements.
 * Handles business logic, database interaction orchestration via {@link AnnouncementRepository},
 * and Data Transfer Object (DTO) mapping.
 */
@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    /**
     * Retrieves all announcements from the database, ordered by their creation date
     * in descending order (newest first), and maps them into DTOs.
     *
     * @return a {@link List} of {@link AnnouncementDto} objects sorted chronologically by newest
     */
    public List<AnnouncementDto> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public AnnouncementDto toDto(Announcement a) {
        return new AnnouncementDto(
                a.getId(),
                a.getTitle(),
                a.getContent(),
                a.getCreator().getUsername(),
                a.getCreatedAt()
        );
    }
}