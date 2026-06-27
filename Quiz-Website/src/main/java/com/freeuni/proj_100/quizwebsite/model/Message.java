package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="messages")
public class Message {
    public enum MessageType {
        friend_request,
        challenge,
        note
    }

    public Message(Long senderId, Long receiverId, MessageType type, String textContent) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
        this.textContent = textContent;
        this.isRead = false;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "msg_type", nullable = false)
    private MessageType type;

    @Column(name = "content")
    private String textContent;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "sent_at", insertable = false, updatable = false)
    private Timestamp sentAt;


    // Getters
    public Long getId() { return id; }
    public Long getSenderId() { return senderId; }
    public Long getReceiverId() { return receiverId; }
    public MessageType getType() { return type; }
    public String getTextContent() { return textContent; }
    public boolean isRead() { return isRead; }
    public Timestamp getSentAt() { return sentAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public void setType(MessageType type) { this.type = type; }
    public void setTextContent(String textContent) { this.textContent = textContent; }
    public void setRead(boolean read) { isRead = read; }
    public void setSentAt(Timestamp sentAt) { this.sentAt = sentAt; }

    public Message() {}
}
