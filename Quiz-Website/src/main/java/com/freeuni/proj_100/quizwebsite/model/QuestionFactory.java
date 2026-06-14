package com.freeuni.proj_100.quizwebsite.model;

import java.util.List;

/**
 * Class responsible for creating question based on question type input.
 */
public class QuestionFactory {
    /**
     *
     * @param questionEntity database entity loaded by Hibernate from "questions" table,
     *                       containing id, quiz id, prompt, type and image url.
     * @param correctAnswers List of correct answers to the question.
     * @param options List of multiple-choice answers.
     * @return Question of the determined type.
     */
    public static Question createQuestion(QuestionEntity questionEntity,
                                          List<String> correctAnswers, List<String> options){
        String questionType = questionEntity.getQ_type();
        if(questionType == null || questionType.isEmpty()){
            throw new IllegalArgumentException("Question type can't be null or empty.");
        }

        return switch (questionType.toLowerCase().trim()) {
            case "text" ->
                    new StandardQuestion(questionEntity.getId(), questionEntity.getQuiz_id(),
                            questionEntity.getPrompt(), questionType, correctAnswers);
            case "fill_blank" ->
                    new FillInTheBlankQuestion(questionEntity.getId(), questionEntity.getQuiz_id(),
                            questionEntity.getPrompt(), questionType, correctAnswers);
            case "multiple_choice" ->
                    new MultipleChoiceQuestion(questionEntity.getId(), questionEntity.getQuiz_id(),
                            questionEntity.getPrompt(), questionType, correctAnswers, options);
            case "picture" ->
                    new PictureResponseQuestion(questionEntity.getId(), questionEntity.getQuiz_id(),
                            questionEntity.getPrompt(), questionType, questionEntity.getImage_url(), correctAnswers);
            default -> throw new IllegalArgumentException("Unknown question type: " + questionType);
        };
    }
}
