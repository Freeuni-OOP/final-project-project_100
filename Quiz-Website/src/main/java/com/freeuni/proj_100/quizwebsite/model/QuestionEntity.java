package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
    private int quizId;

    /**
     * Discriminator string showing question type.
     */
    @Column(name = "q_type", nullable = false)
    private String qType;

    /**
     * Text question or prompt.
     */
    @Column(name = "prompt", nullable = false)
    private String prompt;

    /**
     * URL of the image provided in picture response questions.
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * Sorting integer used to put questions in order in a quiz.
     */
    @Column(name = "sequence_num")
    private int sequenceNum;

    /**
     * Answers belonging to this question.
     * Loaded with an entity graph when quiz questions are fetched.
     */
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @OrderBy("slotNum ASC, id ASC")
    private List<AnswerEntity> answers = new ArrayList<>();

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
     * @param quizId parent quiz identifier.
     */
    public void setQuizId(int quizId) { this.quizId = quizId; }

    /**
     * Gets the foreign key linking this question to a quiz.
     * @return parent quiz identifier.
     */
    public int getQuizId() { return quizId; }

    /**
     * Sets the type classification identifier for this question.
     * @param qType question type discriminator string.
     */
    public void setQType(String qType) { this.qType = qType; }

    /**
     * Gets the type classification identifier for this question.
     * @return question type discriminator string.
     */
    public String getQType() { return qType; }

    /**
     * Sets text prompt of the question.
     * @param prompt text string presented to user.
     */
    public void setPrompt(String prompt) { this.prompt = prompt; }

    /**
     * Gets text prompt of the question.
     * @return text string presented to user.
     */
    public String getPrompt() { return prompt; }

    /**
     * Sets url of the image presented in picture-response question.
     * @param imageUrl string url of the image.
     */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /**
     * Gets url of the image presented in picture-response question.
     * @return string url of the image.
     */
    public String getImageUrl() { return imageUrl; }

    /**
     * Sets display order position for the question.
     * @param sequenceNum sequence tracking integer.
     */
    public void setSequenceNum(int sequenceNum) { this.sequenceNum = sequenceNum; }

    /**
     * Gets display order position for the question.
     * @return sequence tracking integer.
     */
    public int getSequenceNum() { return sequenceNum; }

    /**
     * Gets all answers belonging to this question.
     * @return ordered list of answer entities.
     */
    public List<AnswerEntity> getAnswers() { return answers; }

    /**
     * Sets all answers belonging to this question.
     * @param answers ordered list of answer entities.
     */
    public void setAnswers(List<AnswerEntity> answers) {
        this.answers = answers == null ? new ArrayList<>() : answers;
    }

    /**
     * Backward-compatible setter for older code using database-style naming.
     * @param quiz_id parent quiz identifier.
     */
    public void setQuiz_id(int quiz_id) { this.quizId = quiz_id; }

    /**
     * Backward-compatible getter for older code using database-style naming.
     * @return parent quiz identifier.
     */
    public int getQuiz_id() { return quizId; }

    /**
     * Backward-compatible setter for older code using database-style naming.
     * @param q_type question type discriminator string.
     */
    public void setQ_type(String q_type) { this.qType = q_type; }

    /**
     * Backward-compatible getter for older code using database-style naming.
     * @return question type discriminator string.
     */
    public String getQ_type() { return qType; }

    /**
     * Backward-compatible setter for older code using database-style naming.
     * @param image_url string url of the image.
     */
    public void setImage_url(String image_url) { this.imageUrl = image_url; }

    /**
     * Backward-compatible getter for older code using database-style naming.
     * @return string url of the image.
     */
    public String getImage_url() { return imageUrl; }

    /**
     * Backward-compatible setter for older code using database-style naming.
     * @param sequence_num sequence tracking integer.
     */
    public void setSequence_num(int sequence_num) { this.sequenceNum = sequence_num; }

    /**
     * Backward-compatible getter for older code using database-style naming.
     * @return sequence tracking integer.
     */
    public int getSequence_num() { return sequenceNum; }
}
