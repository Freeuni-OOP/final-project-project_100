package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.model.Quiz;
import com.freeuni.proj_100.quizwebsite.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import com.freeuni.proj_100.quizwebsite.dto.QuizCreationDTO;

import java.util.Optional;

/**
 * Controller for quiz-taking related HTTP requests.
 * Handles routing and passes data to Thymeleaf templates.
 */
@Controller
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    /**
     * Spring automatically injects QuizService here.
     */
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * Shows the quiz summary page before taking the quiz.
     * Accessible at: GET /quiz/{id}
     */
    @GetMapping("/{id}")
    public String quizPage(@PathVariable Integer id, Model model) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("quiz", quiz.get());
        return "quiz/view";
    }

    /**
     * Shows the quiz-taking page.
     * Accessible at: GET /quiz/{id}/take
     */
    @GetMapping("/{id}/take")
    public String takeQuiz(@PathVariable Integer id, Model model) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("quiz", quiz.get());
        return "quiz/take";
    }

    /**
     * Handles quiz submission.
     * Accessible at: POST /quiz/{id}/submit
     */
    @PostMapping("/{id}/submit")
    public String submitQuiz(@PathVariable Integer id,
                             @RequestParam java.util.Map<String, String> answers,
                             Model model) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isEmpty()) {
            return "redirect:/";
        }
        // scoring logic will go here
        model.addAttribute("quiz", quiz.get());
        model.addAttribute("answers", answers);
        return "quiz/results";
    }
    /**
     * Handles incoming React Axios requests to build and save a new quiz.
     */
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createQuiz(@RequestBody QuizCreationDTO quizCreationDTO) {
        if (quizCreationDTO.getTitle() == null || quizCreationDTO.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Quiz title cannot be blank.");
        }

        try {
            quizService.saveQuizFromDTO(quizCreationDTO);
            return ResponseEntity.ok("Quiz created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating quiz: " + e.getMessage());
        }
    }
}