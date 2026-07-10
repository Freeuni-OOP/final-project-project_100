package com.freeuni.proj_100.quizwebsite.dto;

import java.util.List;

public class QuizSummaryDTO {
    private List<QuizAttemptDTO> topAllTime;
    private List<QuizAttemptDTO> topLastDay;
    private List<QuizAttemptDTO> recentTakers;
    private List<QuizAttemptDTO> userHistory;

    // Standard Getters and Setters
    public List<QuizAttemptDTO> getTopAllTime() { return topAllTime; }
    public void setTopAllTime(List<QuizAttemptDTO> topAllTime) { this.topAllTime = topAllTime; }

    public List<QuizAttemptDTO> getTopLastDay() { return topLastDay; }
    public void setTopLastDay(List<QuizAttemptDTO> topLastDay) { this.topLastDay = topLastDay; }

    public List<QuizAttemptDTO> getRecentTakers() { return recentTakers; }
    public void setRecentTakers(List<QuizAttemptDTO> recentTakers) { this.recentTakers = recentTakers; }

    public List<QuizAttemptDTO> getUserHistory() { return userHistory; }
    public void setUserHistory(List<QuizAttemptDTO> userHistory) { this.userHistory = userHistory; }
}