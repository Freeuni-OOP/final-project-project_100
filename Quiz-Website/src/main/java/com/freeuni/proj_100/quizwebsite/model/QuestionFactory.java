package com.freeuni.proj_100.quizwebsite.model;

import java.util.List;

/**
 * Class responsible for creating question based on question type input.
 */
public class QuestionFactory {
    /**
     *
     * @param questionID Unique id of the question.
     * @param quizID Unique id of the quiz this questions belongs to.
     * @param questionPrompt Text prompt of the question.
     * @param questionType Type of question.
     * @param imageUrl Url of the image.
     * @param correctAnswers List of correct answers to the question.
     * @param options List of multiple-choice answers.
     * @return Question of the determined type.
     */
    public static Question createQuestion(int questionID, int quizID,
                                          String questionPrompt, String questionType, String imageUrl,
                                          List<String> correctAnswers, List<String> options){
        if(questionType == null || questionType.isEmpty()){
            throw new IllegalArgumentException("Question type can't be null or empty.");
        }

        return switch (questionType.toUpperCase().trim()) {
            case "STANDARD" ->
                    new StandardQuestion(questionID, quizID, questionPrompt, questionType, correctAnswers);
            case "FILL_IN_BLANK" ->
                    new FillInTheBlankQuestion(questionID, quizID, questionPrompt, questionType, correctAnswers);
            case "MULTIPLE_CHOICE" ->
                    new MultipleChoiceQuestion(questionID, quizID, questionPrompt, questionType, correctAnswers, options);
            case "PICTURE_RESPONSE" ->
                    new PictureResponseQuestion(questionID, quizID, questionPrompt, questionType, imageUrl, correctAnswers);
            default -> throw new IllegalArgumentException("Unknown question type: " + questionType);
        };
    }
}
