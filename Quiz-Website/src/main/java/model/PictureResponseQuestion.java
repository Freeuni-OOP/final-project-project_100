package model;

import java.util.List;
import java.util.Map;

public class PictureResponseQuestion implements Question{
    private final int questionID;
    private final int quizID;
    private final String questionPrompt;
    private final String questionType;
    private final String imageUrl;
    private final List<String> correctAnswers;

    public PictureResponseQuestion(int questionID, int quizID,
                                   String questionPrompt, String questionType,
                                   String imageUrl, List<String> correctAnswers){
        this.questionID = questionID;
        this.quizID = quizID;
        this.questionPrompt = questionPrompt;
        this.questionType = questionType;
        this.imageUrl = imageUrl;
        this.correctAnswers = correctAnswers;
    }

    /**
     * Gets image of the question.
     * @return image url of the image.
     */
    public String getImageUrl(){
        return imageUrl;
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