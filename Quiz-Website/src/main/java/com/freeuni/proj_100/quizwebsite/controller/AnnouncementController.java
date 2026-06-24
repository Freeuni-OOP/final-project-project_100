package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.AnnouncementDto;
import com.freeuni.proj_100.quizwebsite.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing and retrieving system announcements.
 * <p>
 * Provides HTTP endpoints to fetch announcements created by administrators
 * for the quiz website platform.
 * </p>
 */
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    private final AdminService adminService;

    /**
     * Constructs a new {@code AnnouncementController} with the required dependency.
     *
     * @param adminService the service handling administration logic and announcements
     */
    public AnnouncementController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Retrieves a list of all announcements available in the system.
     *
     * @return a {@link ResponseEntity} containing a list of {@link AnnouncementDto} objects 
     * and an HTTP 200 (OK) status
     */
    @GetMapping
    public ResponseEntity<List<AnnouncementDto>> getAnnouncements() {
        return ResponseEntity.ok(adminService.getAllAnnouncements());
    }

}
