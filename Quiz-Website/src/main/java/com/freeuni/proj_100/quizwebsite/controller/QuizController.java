package com.freeuni.proj_100.quizwebsite.controller;

import com.freeuni.proj_100.quizwebsite.model.Quiz;
import com.freeuni.proj_100.quizwebsite.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.freeuni.proj_100.quizwebsite.dto.QuizCreationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;

/**
 * Controller for quiz-taking related HTTP requests.
 */
@Controller
@RequestMapping("/quiz")
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
    public String quizPage(@PathVariable Long id, Model model) {
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
    public String takeQuiz(@PathVariable Long id, Model model) {
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
    public String submitQuiz(@PathVariable Long id,
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
    @PostMapping("/api/quizzes")
    public ResponseEntity<String> createQuiz(@RequestBody QuizCreationDTO quizCreationDTO) {
        // Basic validation check matching requirements
        if (quizCreationDTO.getTitle() == null || quizCreationDTO.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Quiz title cannot be blank.");
        }

        // Printing statements so incoming frontend payloads can immediately be seen in console, Will remove later
        System.out.println("Received Quiz Creation Payload: " + quizCreationDTO.getTitle());
        System.out.println("Number of questions submitted: " + quizCreationDTO.getQuestions().size());

        // TODO: QuizService logic will be called here once Temo integrates database entities

        return ResponseEntity.ok("Quiz base draft received successfully");
    }
}