package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;

/**
 * Data entity class representing a single row from "questions" table,
 * handles Object-Relational mapping for Hibernate.
 */
@Entity
@Table(name = "questions")
public class QuestionEntity {
    /**
     * Auto-incremented and unique primary key identifier for question.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Foreign key referencing unique identifier of the quiz.
     */
    @Column(name = "quiz_id", nullable = false)
    private int quiz_id;

    /**
     * Discriminator string showing question type.
     */
    @Column(name = "q_type", nullable = false)
    private String q_type;

    /**
     * Text question or prompt.
     */
    @Column(name = "prompt", nullable = false)
    private String prompt;

    /**
     * URL of the image provided in picture response questions.
     */
    @Column(name = "image_url")
    private String image_url;

    /**
     * Sorting integer used to put questions in order in a quiz.
     */
    @Column(name = "sequence_num")
    private int sequence_num;

    /**
     * Default constructor required by Hibernate for runtime entity instantiation.
     */
    public QuestionEntity(){}

    /**
     * Sets unique database identifier for this question.
     * @param id primary key integer.
     */
    public void setId(int id) {this.id = id;}
    /**
     * Gets unique database identifier for this question.
     * @return primary key integer.
     */
    public int getId() {return id;}

    /**
     * Sets the foreign key linking this question to a quiz.
     * @param quiz_id parent quiz identifier.
     */
    public void setQuiz_id(int quiz_id) {this.quiz_id = quiz_id;}
    /**
     * Gets the foreign key linking this question to a quiz.
     * @return parent quiz identifier.
     */
    public int getQuiz_id() {return quiz_id;}

    /**
     * Sets the type classification identifier for this question.
     * @param q_type question type discriminator string.
     */
    public void setQ_type(String q_type){this.q_type = q_type;}
    /**
     * Gets the type classification identifier for this question.
     * @return question type discriminator string.
     */
    public String getQ_type(){return q_type;}

    /**
     * Sets text prompt of the question.
     * @param prompt text string presented to user.
     */
    public void setPrompt(String prompt){this.prompt = prompt;}
    /**
     * Gets text prompt of the question.
     * @return text string presented to user.
     */
    public String getPrompt(){return prompt;}

    /**
     * Sets url of the image presented in picture-response question.
     * @param image_url string url of the image.
     */
    public void setImage_url(String image_url){this.image_url = image_url;}
    /**
     * Gets url of the image presented in picture-response question.
     * @return string url of the image.
     */
    public String getImage_url(){return image_url;}

    /**
     * Sets display order position for the question.
     * @param sequence_num sequence tracking integer.
     */
    public void setSequence_num(int sequence_num) {this.sequence_num = sequence_num;}
    /**
     * Gets display order position for the question.
     * @return sequence tracking integer.
     */
    public int getSequence_num() {return sequence_num;}
}
