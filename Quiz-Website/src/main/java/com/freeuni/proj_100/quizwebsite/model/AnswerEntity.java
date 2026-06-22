package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;

/**
 * Data entity class representing a single row from "answers" table.
 * Handles Object-Relational mapping for Hibernate.
 */
@Entity
@Table(name = "answers")
public class AnswerEntity {

    /** Auto-incremented primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Foreign key referencing the question this answer belongs to. */
    @Column(name = "question_id", nullable = false)
    private int questionId;

    /** The answer text. */
    @Column(name = "answer_text", nullable = false)
    private String answerText;

    /** Whether this answer is correct. */
    @Column(name = "is_correct")
    private boolean isCorrect;

    /** Slot number for ordered multi-answer questions. */
    @Column(name = "slot_num")
    private int slotNum;

    public AnswerEntity() {}

    public int getId() { return id; }
    public int getQuestionId() { return questionId; }
    public String getAnswerText() { return answerText; }
    public boolean isCorrect() { return isCorrect; }
    public int getSlotNum() { return slotNum; }

    public void setId(int id) { this.id = id; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
    public void setSlotNum(int slotNum) { this.slotNum = slotNum; }
}