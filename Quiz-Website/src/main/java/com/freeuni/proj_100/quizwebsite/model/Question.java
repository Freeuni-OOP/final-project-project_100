package com.freeuni.proj_100.quizwebsite.model;

import java.util.Map;

/**
 * Generic interface to implement classes for all 4 mandatory
 * question types and more!
 */
public interface Question {
    /**
     * Gets unique id of the question.
     * @return auto-incremented primary key "id" from "questions" table.
     */
    int getQuestionID();

    /**
     * Gets unique id of the quiz this question belongs to.
     * @return foreign key "quiz_id".
     */
    int getQuizID();

    /**
     * Gets text prompt of the question.
     * @return text string from "prompt" column in database.
     */
    String getQuestionPrompt();

    /**
     * Gets type of the question.
     * @return "q_type" value matching one of the question types.
     */
    String getQuestionType();

    /**
     * Checks if users input matches correct answer.
     * @param params a map containing request data sent from client,
     *               where key follows dynamic format "question_[id]".
     * @return true if answer is correct, false otherwise.
     */
    boolean checkAnswer(Map<String, String[]> params);
}