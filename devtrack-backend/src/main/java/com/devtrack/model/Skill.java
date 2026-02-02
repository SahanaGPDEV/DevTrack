package com.devtrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "skills", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "skill_name" }))
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @NotBlank(message = "Skill name is required")
    @Size(max = 100)
    private String skillName;

    @NotNull(message = "Category is required")
    @Enumerated(EnumType.STRING)
    private SkillCategory category;

    @Enumerated(EnumType.STRING)
    private Proficiency proficiency = Proficiency.BEGINNER;

    private Double hoursPracticed = 0.0;

    private Integer projectsCount = 0;

    private LocalDate startedDate;

    private LocalDate lastUsed;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    public enum SkillCategory {
        LANGUAGE, FRAMEWORK, DATABASE, TOOL, OTHER
    }

    public enum Proficiency {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }

    // Constructors
    public Skill() {
    }

    public Skill(User user, String skillName, SkillCategory category) {
        this.user = user;
        this.skillName = skillName;
        this.category = category;
    }

    // Business Methods
    public void updateProficiency() {
        if (hoursPracticed >= 500) {
            this.proficiency = Proficiency.EXPERT;
        } else if (hoursPracticed >= 200) {
            this.proficiency = Proficiency.ADVANCED;
        } else if (hoursPracticed >= 50) {
            this.proficiency = Proficiency.INTERMEDIATE;
        }
    }

    public void addHours(double hours) {
        this.hoursPracticed += hours;
        this.lastUsed = LocalDate.now();
        updateProficiency();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
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

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }

    public Proficiency getProficiency() {
        return proficiency;
    }

    public void setProficiency(Proficiency proficiency) {
        this.proficiency = proficiency;
    }

    public Double getHoursPracticed() {
        return hoursPracticed;
    }

    public void setHoursPracticed(Double hoursPracticed) {
        this.hoursPracticed = hoursPracticed;
    }

    public Integer getProjectsCount() {
        return projectsCount;
    }

    public void setProjectsCount(Integer projectsCount) {
        this.projectsCount = projectsCount;
    }

    public LocalDate getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDate startedDate) {
        this.startedDate = startedDate;
    }

    public LocalDate getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDate lastUsed) {
        this.lastUsed = lastUsed;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
