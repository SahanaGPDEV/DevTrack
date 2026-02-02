package com.devtrack.service;

import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.model.Goal;
import com.devtrack.model.User;
import com.devtrack.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class GoalService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserService userService;

    public Goal createGoal(Long userId, Goal goal) {
        User user = userService.getUserById(userId);
        goal.setUser(user);
        return goalRepository.save(goal);
    }

    public List<Goal> getGoalsByUserId(Long userId) {
        return goalRepository.findByUserUserIdOrderByEndDateAsc(userId);
    }

    public Goal getGoalById(Long goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with id: " + goalId));
    }

    public List<Goal> getActiveGoals(Long userId) {
        return goalRepository.getActiveGoals(userId, LocalDate.now());
    }

    public List<Goal> getCompletedGoals(Long userId) {
        return goalRepository.findByUserUserIdAndIsCompleted(userId, true);
    }

    public Goal updateGoal(Long goalId, Goal goalDetails) {
        Goal goal = getGoalById(goalId);

        if (goalDetails.getTitle() != null)
            goal.setTitle(goalDetails.getTitle());
        if (goalDetails.getDescription() != null)
            goal.setDescription(goalDetails.getDescription());
        if (goalDetails.getGoalType() != null)
            goal.setGoalType(goalDetails.getGoalType());
        if (goalDetails.getCategory() != null)
            goal.setCategory(goalDetails.getCategory());
        if (goalDetails.getTargetValue() != null)
            goal.setTargetValue(goalDetails.getTargetValue());
        if (goalDetails.getCurrentValue() != null)
            goal.setCurrentValue(goalDetails.getCurrentValue());
        if (goalDetails.getStartDate() != null)
            goal.setStartDate(goalDetails.getStartDate());
        if (goalDetails.getEndDate() != null)
            goal.setEndDate(goalDetails.getEndDate());
        if (goalDetails.getPriority() != null)
            goal.setPriority(goalDetails.getPriority());

        // Check if goal is completed
        if (goal.getCurrentValue() >= goal.getTargetValue()) {
            goal.setIsCompleted(true);
            goal.setCompletedDate(LocalDate.now());
        }

        return goalRepository.save(goal);
    }

    public Goal updateProgress(Long goalId, Integer progress) {
        Goal goal = getGoalById(goalId);
        goal.updateProgress(progress);
        return goalRepository.save(goal);
    }

    public void deleteGoal(Long goalId) {
        Goal goal = getGoalById(goalId);
        goalRepository.delete(goal);
    }

    public Integer countCompletedGoals(Long userId) {
        return goalRepository.countCompletedGoals(userId);
    }
}
