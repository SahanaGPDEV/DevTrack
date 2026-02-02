package com.devtrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "dsa_problems")
public class DsaProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @NotBlank(message = "Problem title is required")
    @Size(max = 200)
    private String title;

    @NotNull(message = "Platform is required")
    @Enumerated(EnumType.STRING)
    private Platform platform;

    @NotNull(message = "Difficulty is required")
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private String topic;

    @Size(max = 500)
    private String problemLink;

    @Enumerated(EnumType.STRING)
    private Status status = Status.SOLVED;

    private Integer timeTaken;

    @Column(columnDefinition = "TEXT")
    private String approach;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @NotNull(message = "Solved date is required")
    private LocalDate solvedDate;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Platform {
        LEETCODE, HACKERRANK, CODECHEF, CODEFORCES, GFG, OTHER
    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public enum Status {
        SOLVED, ATTEMPTED, REVISIT
    }

    // Constructors
    public DsaProblem() {
    }

    public DsaProblem(User user, String title, Platform platform, Difficulty difficulty, LocalDate solvedDate) {
        this.user = user;
        this.title = title;
        this.platform = platform;
        this.difficulty = difficulty;
        this.solvedDate = solvedDate;
    }

    // Helper method for difficulty points
    public int getDifficultyPoints() {
        return switch (this.difficulty) {
            case EASY -> 1;
            case MEDIUM -> 2;
            case HARD -> 3;
        };
    }

    // Getters and Setters
    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getUserId() : null;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getProblemLink() {
        return problemLink;
    }

    public void setProblemLink(String problemLink) {
        this.problemLink = problemLink;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Integer timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getApproach() {
        return approach;
    }

    public void setApproach(String approach) {
        this.approach = approach;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getSolvedDate() {
        return solvedDate;
    }

    public void setSolvedDate(LocalDate solvedDate) {
        this.solvedDate = solvedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
