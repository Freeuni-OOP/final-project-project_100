package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.AnnouncementDto;
import com.freeuni.proj_100.quizwebsite.dto.CreateAnnouncementRequest;
import com.freeuni.proj_100.quizwebsite.dto.UserSummaryDto;
import com.freeuni.proj_100.quizwebsite.dto.SiteStatsDto;
import com.freeuni.proj_100.quizwebsite.service.AdminService;
import com.freeuni.proj_100.quizwebsite.service.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for administrative operations.
 * <p>
 * Provides protected endpoints for system administrators to manage users, quizzes, 
 * announcement creation, and retrieve application performance statistics.
 * </p>
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final AnnouncementService announcementService;

    public AdminController(
            AdminService adminService,
            AnnouncementService announcementService
    ) {
        this.adminService = adminService;
        this.announcementService = announcementService;
    }

    /**
     * Creates a new global site announcement.
     * <p>
     * Map to HTTP POST {@code /api/admin/announcements}.
     * </p>
     *
     * @param req      the validated request body containing announcement details
     * @param username the username of the authenticated administrator creating the announcement
     * @return a {@link ResponseEntity} containing the created {@link AnnouncementDto}
     * and an HTTP 201 (Created) status
     */
    @PostMapping("/announcements")
    public ResponseEntity<AnnouncementDto> createAnnouncement(
            @Valid @RequestBody CreateAnnouncementRequest req,
            @AuthenticationPrincipal String username
    ) {
        return ResponseEntity.status(201).body(adminService.createAnnouncement(req, username));
    }

    /**
     * Permanently deletes a user from the system.
     * <p>
     * Map to HTTP DELETE {@code /api/admin/users/{id}}.
     * </p>
     *
     * @param id the unique identifier of the user to be deleted
     * @return a {@link ResponseEntity} with an HTTP 244 (No Content) status upon successful execution
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Permanently deletes a quiz from the system.
     * <p>
     * Map to HTTP DELETE {@code /api/admin/quiz/{id}}.
     * </p>
     *
     * @param id the unique identifier of the quiz to be deleted
     * @return a {@link ResponseEntity} with an HTTP 204 (No Content) status upon successful execution
     */
    @DeleteMapping("/quiz/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        adminService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Clears all participation and performance history records associated with a specific quiz.
     * <p>
     * Map to HTTP DELETE {@code /api/admin/quiz/{id}/history}.
     * </p>
     *
     * @param id the unique identifier of the quiz whose history should be wiped
     * @return a {@link ResponseEntity} with an HTTP 204 (No Content) status upon successful execution
     */
    @DeleteMapping("/quiz/{id}/history")
    public ResponseEntity<Void> deleteQuizHistory(@PathVariable Long id) {
        adminService.clearQuizHistory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Promotes a regular user account to an administrative role.
     * <p>
     * Map to HTTP PUT {@code /api/admin/users/{id}/promote}.
     * </p>
     *
     * @param id the unique identifier of the user to be promoted
     * @return a {@link ResponseEntity} with an HTTP 204 (No Content) status upon successful execution
     */
    @PutMapping("/users/{id}/promote")
    public ResponseEntity<Void> promoteToAdmin(@PathVariable Long id) {
        adminService.promoteToAdmin(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves statistics for the website.
     * <p>
     * Map to HTTP GET {@code /api/admin/stats}.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a {@link SiteStatsDto} and an HTTP 200 (OK) status
     */
    @GetMapping("/stats")
    public ResponseEntity<SiteStatsDto> getSiteStats() {
        return ResponseEntity.ok(adminService.getSiteStats());
    }

    /**
     * Retrieves all announcements managed within the administrative domain.
     * <p>
     * Map to HTTP GET {@code /api/admin/announcements}.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a list of {@link AnnouncementDto} records 
     * and an HTTP 200 (OK) status
     */
    @GetMapping("/announcements")
    public ResponseEntity<List<AnnouncementDto>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserSummaryDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }
}
