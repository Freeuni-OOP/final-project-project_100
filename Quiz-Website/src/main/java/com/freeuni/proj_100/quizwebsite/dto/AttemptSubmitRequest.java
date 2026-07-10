package com.freeuni.proj_100.quizwebsite.dto;

import java.util.List;

public class AttemptSubmitRequest {

    private List<AnswerSubmission> answers;
    private boolean isPractice;
    private int timeTakenSec;

    public AttemptSubmitRequest() {}

    public List<AnswerSubmission> getAnswers() { return answers; }
    public boolean isPractice() { return isPractice; }
    public int getTimeTakenSec() { return timeTakenSec; }

    public void setAnswers(List<AnswerSubmission> answers) { this.answers = answers; }
    public void setPractice(boolean practice) { isPractice = practice; }
    public void setTimeTakenSec(int timeTakenSec) { this.timeTakenSec = timeTakenSec; }

    public static class AnswerSubmission {
        private Integer questionId;
        private String response;

        public AnswerSubmission() {}

        public Integer getQuestionId() { return questionId; }
        public String getResponse() { return response; }

        public void setQuestionId(Integer questionId) { this.questionId = questionId; }
        public void setResponse(String response) { this.response = response; }
    }
}