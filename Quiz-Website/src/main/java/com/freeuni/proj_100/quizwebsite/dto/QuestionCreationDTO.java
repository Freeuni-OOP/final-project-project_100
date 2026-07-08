package com.freeuni.proj_100.quizwebsite.dto;

import java.util.List;

public class QuestionCreationDTO {
    private String type;
    private String questionText;
    private List<String> options;
    private String correctAnswer;
    private String imageUrl; // Supports Picture-Response type

    // Default constructor required for Jackson JSON parsing
    public QuestionCreationDTO() {}

    // Getters and Setters
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }
    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}