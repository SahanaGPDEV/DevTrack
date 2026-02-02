package com.devtrack.controller;

import com.devtrack.dto.ApiResponse;
import com.devtrack.model.Goal;
import com.devtrack.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Goal>> createGoal(
            @PathVariable Long userId,
            @Valid @RequestBody Goal goal) {
        Goal createdGoal = goalService.createGoal(userId, goal);
        return ResponseEntity.ok(ApiResponse.success("Goal created", createdGoal));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Goal>>> getGoalsByUser(@PathVariable Long userId) {
        List<Goal> goals = goalService.getGoalsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(goals));
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<ApiResponse<Goal>> getGoalById(@PathVariable Long goalId) {
        Goal goal = goalService.getGoalById(goalId);
        return ResponseEntity.ok(ApiResponse.success(goal));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<ApiResponse<List<Goal>>> getActiveGoals(@PathVariable Long userId) {
        List<Goal> goals = goalService.getActiveGoals(userId);
        return ResponseEntity.ok(ApiResponse.success(goals));
    }

    @GetMapping("/user/{userId}/completed")
    public ResponseEntity<ApiResponse<List<Goal>>> getCompletedGoals(@PathVariable Long userId) {
        List<Goal> goals = goalService.getCompletedGoals(userId);
        return ResponseEntity.ok(ApiResponse.success(goals));
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<ApiResponse<Goal>> updateGoal(
            @PathVariable Long goalId,
            @RequestBody Goal goal) {
        Goal updatedGoal = goalService.updateGoal(goalId, goal);
        return ResponseEntity.ok(ApiResponse.success("Goal updated", updatedGoal));
    }

    @PutMapping("/{goalId}/progress")
    public ResponseEntity<ApiResponse<Goal>> updateProgress(
            @PathVariable Long goalId,
            @RequestParam Integer progress) {
        Goal updatedGoal = goalService.updateProgress(goalId, progress);
        return ResponseEntity.ok(ApiResponse.success("Progress updated", updatedGoal));
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<ApiResponse<Void>> deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
        return ResponseEntity.ok(ApiResponse.success("Goal deleted", null));
    }
}
