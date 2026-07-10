package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.AnnouncementDto;
import com.freeuni.proj_100.quizwebsite.dto.CreateAnnouncementRequest;
import com.freeuni.proj_100.quizwebsite.dto.SiteStatsDto;
import com.freeuni.proj_100.quizwebsite.dto.UserSummaryDto;
import com.freeuni.proj_100.quizwebsite.exception.AuthException;
import com.freeuni.proj_100.quizwebsite.exception.ResourceNotFoundException;
import com.freeuni.proj_100.quizwebsite.model.Announcement;
import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.repository.AnnouncementRepository;
import com.freeuni.proj_100.quizwebsite.repository.QuizAttemptRepository;
import com.freeuni.proj_100.quizwebsite.repository.QuizRepository;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class containing business logic for administrative operations.
 * <p>
 * This service is secured globally and requires the caller to possess the {@code 'ROLE_ADMIN'} authority.
 * It manages users, quizzes, system-wide announcements, and aggregates site telemetry statistics.
 * </p>
 */
@Service
@PreAuthorize("hasRole('ADMIN')")
public class AdminService {

    private final UserRepository userRepo;
    private final QuizRepository quizRepo;
    private final QuizAttemptRepository quizAttemptRepo;
    private final AnnouncementRepository announcementRepo;
    private final AnnouncementService announcementService;

    /**
     * Constructs an {@code AdminService} with all required data repositories.
     *
     * @param userRepo         the repository for user persistence operations
     * @param quizRepo         the repository for quiz management
     * @param quizAttemptRepo  the repository for tracking quiz history and attempts
     * @param announcementRepo the repository for platform announcements
     */
    public AdminService(
            UserRepository userRepo,
            QuizRepository quizRepo,
            QuizAttemptRepository quizAttemptRepo,
            AnnouncementRepository announcementRepo,
            AnnouncementService announcementService
    ) {
        this.userRepo = userRepo;
        this.quizRepo = quizRepo;
        this.announcementRepo = announcementRepo;
        this.quizAttemptRepo = quizAttemptRepo;
        this.announcementService = announcementService;
    }

    /**
     * Creates and saves a new system-wide announcement.
     *
     * @param req      the payload containing the title and content of the announcement
     * @param username the username of the administrator creating the entry
     * @return an {@link AnnouncementDto} representation of the newly created entity
     * @throws AuthException if the administrator account cannot be verified in the system
     */
    public AnnouncementDto createAnnouncement(
            CreateAnnouncementRequest req,
            String username
    ) {
        User creator = userRepo.findByUsername(username)
                .orElseThrow(() -> new AuthException("User not found"));

        Announcement announcement = new Announcement();
        announcement.setTitle(req.title());
        announcement.setContent(req.content());
        announcement.setCreator(creator);

        Announcement saved = announcementRepo.save(announcement);

        return announcementService.toDto(saved);
    }

    /**
     *  Retrieves all user summaries
     * @return
     */
    public List<UserSummaryDto> getAllUsers() {
        return userRepo.findAll().stream()
                .map(u -> new UserSummaryDto(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.isAdmin(),
                        u.getCreatedAt()
                ))
                .toList();
    }

    /**
     * Deletes a user from the platform database by their unique ID.
     * <p>
     * Guarded by a transaction. This action will fail intentionally if the target user
     * is also an administrator.
     * </p>
     *
     * @param userId the unique identifier of the user account to remove
     * @throws AuthException if the target user is not found, or if they hold administrative rights
     */
    @Transactional
    public void deleteUser(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AuthException("User not found"));

        if (user.isAdmin()) {
            throw new AuthException("Cannot delete another admin account");
        }

        userRepo.delete(user);
    }

    /**
     * Permanently removes a quiz from the system.
     *
     * @param quizId the unique identifier of the quiz to delete
     * @throws ResourceNotFoundException if no quiz matches the provided identifier
     */
    @Transactional
    public void deleteQuiz(Integer quizId) {
        if (!quizRepo.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz not found");
        }

        quizRepo.deleteById(quizId);
    }

    /**
     * Wipes out all history, records, and statistics of user submissions for a specific quiz.
     *
     * @param quizId the unique identifier of the target quiz
     * @throws ResourceNotFoundException if the quiz record does not exist
     */
    @Transactional
    public void clearQuizHistory(Integer quizId) {
        if (!quizRepo.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz not found");
        }

        quizAttemptRepo.deleteByQuizId(quizId);
    }

    /**
     * Promotes an existing platform user account to have administrator privileges.
     *
     * @param userId the unique identifier of the user to promote
     * @throws AuthException if the user is not found, or is already configured as an administrator
     */
    @Transactional
    public void promoteToAdmin(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AuthException("User not found"));

        if (user.isAdmin()) {
            throw new AuthException("User is already an admin");
        }

        user.promoteToAdmin();
        userRepo.save(user);
    }

    /**
     * Computes high-level data aggregation and user acquisition metrics across the portal.
     * Calculates user registrations recorded since the beginning of the current calendar day.
     *
     * @return a {@link SiteStatsDto} containing absolute system metrics
     */
    public SiteStatsDto getSiteStats() {
        long totalUsers = userRepo.count();
        long totalQuizzes = quizRepo.count();
        long totalAttempts = quizAttemptRepo.count();
        long newUsersToday = userRepo.countByCreatedAtAfter(
                LocalDateTime.now().toLocalDate().atStartOfDay()
        );

        return new SiteStatsDto(totalUsers, totalQuizzes, totalAttempts, newUsersToday);
    }
}