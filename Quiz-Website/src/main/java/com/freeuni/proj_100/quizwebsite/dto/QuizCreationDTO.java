package com.freeuni.proj_100.quizwebsite.dto;

import java.util.List;

public class QuizCreationDTO {
    private String title;
    private String description;
    private boolean randomizeQuestions;
    private boolean singlePageLayout;
    private boolean immediateFeedback;
    private List<QuestionCreationDTO> questions;

    public QuizCreationDTO() {}

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isRandomizeQuestions() { return randomizeQuestions; }
    public void setRandomizeQuestions(boolean randomizeQuestions) { this.randomizeQuestions = randomizeQuestions; }

    public boolean isSinglePageLayout() { return singlePageLayout; }
    public void setSinglePageLayout(boolean singlePageLayout) { this.singlePageLayout = singlePageLayout; }

    public boolean isImmediateFeedback() { return immediateFeedback; }
    public void setImmediateFeedback(boolean immediateFeedback) { this.immediateFeedback = immediateFeedback; }

    public List<QuestionCreationDTO> getQuestions() { return questions; }
    public void setQuestions(List<QuestionCreationDTO> questions) { this.questions = questions; }
}