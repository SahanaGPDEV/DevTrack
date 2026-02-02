package com.devtrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "daily_logs", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "log_date" }))
public class DailyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @NotNull(message = "Log date is required")
    private LocalDate logDate;

    @NotNull(message = "Hours coded is required")
    @DecimalMin(value = "0.0", message = "Hours must be positive")
    @DecimalMax(value = "24.0", message = "Hours cannot exceed 24")
    private Double hoursCoded;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String technologiesUsed;

    @Enumerated(EnumType.STRING)
    private Mood mood = Mood.OKAY;

    @Min(value = 1, message = "Productivity score must be at least 1")
    @Max(value = 10, message = "Productivity score cannot exceed 10")
    private Integer productivityScore;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    public enum Mood {
        GREAT, GOOD, OKAY, TIRED, FRUSTRATED
    }

    // Constructors
    public DailyLog() {
    }

    public DailyLog(User user, LocalDate logDate, Double hoursCoded) {
        this.user = user;
        this.logDate = logDate;
        this.hoursCoded = hoursCoded;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
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

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public Double getHoursCoded() {
        return hoursCoded;
    }

    public void setHoursCoded(Double hoursCoded) {
        this.hoursCoded = hoursCoded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnologiesUsed() {
        return technologiesUsed;
    }

    public void setTechnologiesUsed(String technologiesUsed) {
        this.technologiesUsed = technologiesUsed;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public Integer getProductivityScore() {
        return productivityScore;
    }

    public void setProductivityScore(Integer productivityScore) {
        this.productivityScore = productivityScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
