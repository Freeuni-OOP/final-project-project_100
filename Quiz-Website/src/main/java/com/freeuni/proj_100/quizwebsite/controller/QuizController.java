package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.dto.QuizCreationDTO;
import com.freeuni.proj_100.quizwebsite.dto.QuizSummaryServiceDto;
import com.freeuni.proj_100.quizwebsite.exception.ResourceNotFoundException;
import com.freeuni.proj_100.quizwebsite.model.Quiz;
import com.freeuni.proj_100.quizwebsite.model.User;
import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import com.freeuni.proj_100.quizwebsite.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final UserRepository userRepository;

    public QuizController(QuizService quizService, UserRepository userRepository) {
        this.quizService = quizService;
        this.userRepository = userRepository;
    }

    // GET /api/quizzes — list all quizzes
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    // GET /api/quizzes/{id}/start?practice=false — load quiz for taking
    @GetMapping("/{id}/start")
    public ResponseEntity<Quiz> startQuiz(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "false") boolean practice
    ) {
        Quiz quiz = quizService.getQuizForTaking(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + id));
        return ResponseEntity.ok(quiz);
    }

    // POST /api/quizzes/create — create a new quiz
    @PostMapping("create")
    public ResponseEntity<String> createQuiz(
            @RequestBody QuizCreationDTO dto,
            @AuthenticationPrincipal String username
    ) {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Quiz title cannot be blank");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        quizService.saveQuizFromDTO(dto, user.getId());
        return ResponseEntity.status(201).body("Quiz created successfully");
    }

    // GET /api/quizzes/user/{userId} — get quizzes by creator
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Quiz>> getQuizzesByCreator(@PathVariable Integer userId) {
        return ResponseEntity.ok(quizService.getQuizzesByCreator(userId));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<QuizSummaryServiceDto>> getRecentQuizzes() {
        return ResponseEntity.ok(quizService.getRecentQuizzes());
    }
}