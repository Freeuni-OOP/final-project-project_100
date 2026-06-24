package com.freeuni.proj_100.quizwebsite.service;

import com.freeuni.proj_100.quizwebsite.dto.AnnouncementDto;
import com.freeuni.proj_100.quizwebsite.dto.CreateAnnouncementRequest;
import com.freeuni.proj_100.quizwebsite.dto.SiteStatsDto;
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

@Service
@PreAuthorize("hasRole('ADMIN')")
public class AdminService {

    private final UserRepository userRepo;
    private final QuizRepository quizRepo;
    private final QuizAttemptRepository quizAttemptRepo;
    private final AnnouncementRepository announcementRepo;

    public AdminService(
            UserRepository userRepo,
            QuizRepository quizRepo,
            QuizAttemptRepository quizAttemptRepo,
            AnnouncementRepository announcementRepo
    ) {
        this.userRepo = userRepo;
        this.quizRepo = quizRepo;
        this.announcementRepo = announcementRepo;
        this.quizAttemptRepo = quizAttemptRepo;
    }

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
        
        return toDto(saved);
    }

    public List<AnnouncementDto> getAllAnnouncements() {
        return announcementRepo.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AuthException("User not found"));

        if (user.isAdmin()) {
            throw new AuthException("Cannot delete another admin account");
        }

        userRepo.delete(user);
    }

    @Transactional
    public void deleteQuiz(Long quizId) {
        if (!quizRepo.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz not found");
        }

        quizRepo.deleteById(quizId);
    }

    @Transactional
    public void clearQuizHistory(Long quizId) {
        if (!quizRepo.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz not found");
        }

        quizAttemptRepo.deleteByQuizId(quizId);
    }

    @Transactional
    public void promoteToAdmin(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AuthException("User not found"));

        if (user.isAdmin()) {
            throw new AuthException("User is already an admin");
        }

        user.promoteToAdmin();
        userRepo.save(user);
    }

    public SiteStatsDto getSiteStats() {
        long totalUsers = userRepo.count();
        long totalQuizzes = quizRepo.count();
        long totalAttempts = quizAttemptRepo.count();
        long newUsersToday = userRepo.countByCreatedAtAfter(
                LocalDateTime.now().toLocalDate().atStartOfDay()
        );

        return new SiteStatsDto(totalUsers, totalQuizzes, totalAttempts, newUsersToday);
    }

    private AnnouncementDto toDto(Announcement a) {
        return new AnnouncementDto(
                a.getId(),
                a.getTitle(),
                a.getContent(),
                a.getCreator().getUsername(),
                a.getCreatedAt()
        );
    }
}
