package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;

/**
 * Entity representing one row in the "answers" table.
 * Stores both correct answers and multiple-choice options for a question.
 */
@Entity
@Table(name = "answers")
public class AnswerEntity {
    /**
     * Auto-incremented primary key identifier for the answer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Foreign key referencing the question this answer belongs to.
     */
    @Column(name = "question_id", nullable = false)
    private int questionId;

    /**
     * Parent question entity.
     * This is read through the question_id column and is not written separately.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    private QuestionEntity question;

    /**
     * Text value of the answer or multiple-choice option.
     */
    @Column(name = "answer_text", nullable = false)
    private String answerText;

    /**
     * Marks whether this answer is correct.
     */
    @Column(name = "is_correct")
    private boolean correct;

    /**
     * Position used for ordered answers or answer options.
     */
    @Column(name = "slot_num")
    private int slotNum;

    /**
     * Default constructor required by JPA.
     */
    public AnswerEntity() {}

    public int getId() { return id; }
    public int getQuestionId() { return questionId; }
    public QuestionEntity getQuestion() { return question; }
    public String getAnswerText() { return answerText; }
    public boolean isCorrect() { return correct; }
    public int getSlotNum() { return slotNum; }

    public void setId(int id) { this.id = id; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public void setQuestion(QuestionEntity question) { this.question = question; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
    public void setCorrect(boolean correct) { this.correct = correct; }
    public void setSlotNum(int slotNum) { this.slotNum = slotNum; }
}