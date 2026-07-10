package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Database entity representing a platform announcement.
 * <p>
 * Maps to the {@code announcements} table and holds content created by administrators 
 * to be broadcast globally across the application.
 * </p>
 */
@Entity
@Table(name="announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="creator_id", nullable=false)
    private User creator;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false, columnDefinition="TEXT")
    private String content;

    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt;

    public Announcement() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Integer getId() { return id; }
    public User getCreator() { return creator; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreator(User creator) { this.creator = creator; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
}
