package com.devtrack.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsDTO {

    // Overview Stats
    private Integer totalHoursCoded;
    private Integer totalProblemsSolved;
    private Integer currentStreak;
    private Integer longestStreak;
    private Integer activeGoals;
    private Integer completedGoals;
    private Integer totalSkills;

    // Weekly Stats
    private Double weeklyHours;
    private Integer weeklyProblems;
    private Double avgHoursPerDay;

    // Charts Data
    private List<DailyHoursData> dailyHoursChart;
    private Map<String, Integer> problemsByDifficulty;
    private Map<String, Integer> problemsByTopic;
    private List<GoalProgress> goalProgress;

    // Nested Classes for Chart Data
    public static class DailyHoursData {
        private String date;
        private Double hours;

        public DailyHoursData(String date, Double hours) {
            this.date = date;
            this.hours = hours;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Double getHours() {
            return hours;
        }

        public void setHours(Double hours) {
            this.hours = hours;
        }
    }

    public static class GoalProgress {
        private String title;
        private Integer current;
        private Integer target;
        private Double percentage;

        public GoalProgress(String title, Integer current, Integer target) {
            this.title = title;
            this.current = current;
            this.target = target;
            this.percentage = target > 0 ? (current * 100.0) / target : 0;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getCurrent() {
            return current;
        }

        public void setCurrent(Integer current) {
            this.current = current;
        }

        public Integer getTarget() {
            return target;
        }

        public void setTarget(Integer target) {
            this.target = target;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }

    // Getters and Setters
    public Integer getTotalHoursCoded() {
        return totalHoursCoded;
    }

    public void setTotalHoursCoded(Integer totalHoursCoded) {
        this.totalHoursCoded = totalHoursCoded;
    }

    public Integer getTotalProblemsSolved() {
        return totalProblemsSolved;
    }

    public void setTotalProblemsSolved(Integer totalProblemsSolved) {
        this.totalProblemsSolved = totalProblemsSolved;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }

    public Integer getActiveGoals() {
        return activeGoals;
    }

    public void setActiveGoals(Integer activeGoals) {
        this.activeGoals = activeGoals;
    }

    public Integer getCompletedGoals() {
        return completedGoals;
    }

    public void setCompletedGoals(Integer completedGoals) {
        this.completedGoals = completedGoals;
    }

    public Integer getTotalSkills() {
        return totalSkills;
    }

    public void setTotalSkills(Integer totalSkills) {
        this.totalSkills = totalSkills;
    }

    public Double getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(Double weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public Integer getWeeklyProblems() {
        return weeklyProblems;
    }

    public void setWeeklyProblems(Integer weeklyProblems) {
        this.weeklyProblems = weeklyProblems;
    }

    public Double getAvgHoursPerDay() {
        return avgHoursPerDay;
    }

    public void setAvgHoursPerDay(Double avgHoursPerDay) {
        this.avgHoursPerDay = avgHoursPerDay;
    }

    public List<DailyHoursData> getDailyHoursChart() {
        return dailyHoursChart;
    }

    public void setDailyHoursChart(List<DailyHoursData> dailyHoursChart) {
        this.dailyHoursChart = dailyHoursChart;
    }

    public Map<String, Integer> getProblemsByDifficulty() {
        return problemsByDifficulty;
    }

    public void setProblemsByDifficulty(Map<String, Integer> problemsByDifficulty) {
        this.problemsByDifficulty = problemsByDifficulty;
    }

    public Map<String, Integer> getProblemsByTopic() {
        return problemsByTopic;
    }

    public void setProblemsByTopic(Map<String, Integer> problemsByTopic) {
        this.problemsByTopic = problemsByTopic;
    }

    public List<GoalProgress> getGoalProgress() {
        return goalProgress;
    }

    public void setGoalProgress(List<GoalProgress> goalProgress) {
        this.goalProgress = goalProgress;
    }
}
