package model;

import java.util.List;
import java.util.Map;

public class StandardQuestion implements Question{
    private final int questionID;
    private final int quizID;
    private final String questionPrompt;
    private final String questionType;
    private final List<String> correctAnswers;

    public StandardQuestion(int questionID, int quizID,
                            String questionPrompt, String questionType, List<String> correctAnswers){
        this.questionID = questionID;
        this.quizID = quizID;
        this.questionPrompt = questionPrompt;
        this.questionType = questionType;
        this.correctAnswers = correctAnswers;
    }

    @Override
    public int getQuestionID() {
        return questionID;
    }

    @Override
    public int getQuizID() {
        return quizID;
    }

    @Override
    public String getQuestionPrompt() {
        return questionPrompt;
    }

    @Override
    public String getQuestionType() {
        return questionType;
    }

    @Override
    public boolean checkAnswer(Map<String, String[]> params) {
        String paramName = "question_" + questionID;

        if(params == null || !params.containsKey(paramName)) return false;

        String[] userAnswers = params.get(paramName);
        if(userAnswers == null || userAnswers.length == 0) return false;

        String answer = userAnswers[0].trim();
        for(String correctAnswer : correctAnswers){
            if(correctAnswer.trim().equalsIgnoreCase(answer)){
                return true;
            }
        }
        return false;
    }
}