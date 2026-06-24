package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.AnnouncementDto;
import com.freeuni.proj_100.quizwebsite.dto.CreateAnnouncementRequest;
import com.freeuni.proj_100.quizwebsite.dto.SiteStatsDto;
import com.freeuni.proj_100.quizwebsite.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/announcements")
    public ResponseEntity<AnnouncementDto> createAnnouncement(
            @Valid @RequestBody CreateAnnouncementRequest req,
            @AuthenticationPrincipal String username
    ) {
        return ResponseEntity.status(201).body(adminService.createAnnouncement(req, username));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/quiz/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        adminService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/quiz/{id}/history")
    public ResponseEntity<Void> deleteQuizHistory(@PathVariable Long id) {
        adminService.clearQuizHistory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}/promote")
    public ResponseEntity<Void> promoteToAdmin(@PathVariable Long id) {
        adminService.promoteToAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<SiteStatsDto> getSiteStats() {
        return ResponseEntity.ok(adminService.getSiteStats());
    }

    @GetMapping("/announcements")
    public ResponseEntity<List<AnnouncementDto>> getAllAnnouncements() {
        return ResponseEntity.ok(adminService.getAllAnnouncements());
    }
}
