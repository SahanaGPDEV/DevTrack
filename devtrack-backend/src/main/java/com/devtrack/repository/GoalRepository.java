package com.devtrack.repository;

import com.devtrack.model.Goal;
import com.devtrack.model.Goal.GoalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUserUserIdOrderByEndDateAsc(Long userId);

    List<Goal> findByUserUserIdAndIsCompleted(Long userId, Boolean isCompleted);

    List<Goal> findByUserUserIdAndGoalType(Long userId, GoalType goalType);

    @Query("SELECT g FROM Goal g WHERE g.user.userId = :userId " +
            "AND g.isCompleted = false AND g.endDate >= :today ORDER BY g.endDate ASC")
    List<Goal> getActiveGoals(@Param("userId") Long userId, @Param("today") LocalDate today);

    @Query("SELECT g FROM Goal g WHERE g.user.userId = :userId " +
            "AND g.isCompleted = false AND g.endDate < :today")
    List<Goal> getOverdueGoals(@Param("userId") Long userId, @Param("today") LocalDate today);

    @Query("SELECT COUNT(g) FROM Goal g WHERE g.user.userId = :userId AND g.isCompleted = true")
    Integer countCompletedGoals(@Param("userId") Long userId);

    Integer countByUserUserId(Long userId);
}
