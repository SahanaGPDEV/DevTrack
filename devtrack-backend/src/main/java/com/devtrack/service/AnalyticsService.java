package com.devtrack.service;

import com.devtrack.dto.AnalyticsDTO;
import com.devtrack.dto.AnalyticsDTO.DailyHoursData;
import com.devtrack.dto.AnalyticsDTO.GoalProgress;
import com.devtrack.model.DailyLog;
import com.devtrack.model.DsaProblem.Difficulty;
import com.devtrack.model.Goal;
import com.devtrack.model.User;
import com.devtrack.repository.DailyLogRepository;
import com.devtrack.repository.DsaProblemRepository;
import com.devtrack.repository.GoalRepository;
import com.devtrack.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private UserService userService;

    @Autowired
    private DailyLogRepository dailyLogRepository;

    @Autowired
    private DsaProblemRepository dsaProblemRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private SkillRepository skillRepository;

    public AnalyticsDTO getAnalytics(Long userId) {
        User user = userService.getUserById(userId);
        AnalyticsDTO analytics = new AnalyticsDTO();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);

        // Overview Stats
        analytics.setTotalHoursCoded(user.getTotalHoursCoded().intValue());
        analytics.setTotalProblemsSolved(user.getTotalProblemsSolved());
        analytics.setCurrentStreak(user.getStreakCount());
        analytics.setLongestStreak(user.getLongestStreak());

        // Goals Stats
        List<Goal> activeGoals = goalRepository.getActiveGoals(userId, today);
        analytics.setActiveGoals(activeGoals.size());
        analytics.setCompletedGoals(goalRepository.countCompletedGoals(userId));

        // Skills Stats
        analytics.setTotalSkills(skillRepository.countByUserUserId(userId));

        // Weekly Stats
        Double weeklyHours = dailyLogRepository.getTotalHoursInRange(userId, weekStart, today);
        analytics.setWeeklyHours(weeklyHours != null ? weeklyHours : 0.0);

        Integer weeklyProblems = dsaProblemRepository.countProblemsInRange(userId, weekStart, today);
        analytics.setWeeklyProblems(weeklyProblems != null ? weeklyProblems : 0);

        Double avgHours = dailyLogRepository.getAverageHoursPerDay(userId);
        analytics.setAvgHoursPerDay(avgHours != null ? Math.round(avgHours * 10) / 10.0 : 0.0);

        // Daily Hours Chart (Last 7 days)
        List<DailyLog> logs = dailyLogRepository.findByUserIdAndDateRange(userId, weekStart, today);
        List<DailyHoursData> dailyHoursChart = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            Double hours = logs.stream()
                    .filter(log -> log.getLogDate().equals(date))
                    .map(DailyLog::getHoursCoded)
                    .findFirst()
                    .orElse(0.0);
            dailyHoursChart.add(new DailyHoursData(date.format(formatter), hours));
        }
        analytics.setDailyHoursChart(dailyHoursChart);

        // Problems by Difficulty
        Map<String, Integer> problemsByDifficulty = new HashMap<>();
        problemsByDifficulty.put("EASY", dsaProblemRepository.countByUserIdAndDifficulty(userId, Difficulty.EASY));
        problemsByDifficulty.put("MEDIUM", dsaProblemRepository.countByUserIdAndDifficulty(userId, Difficulty.MEDIUM));
        problemsByDifficulty.put("HARD", dsaProblemRepository.countByUserIdAndDifficulty(userId, Difficulty.HARD));
        analytics.setProblemsByDifficulty(problemsByDifficulty);

        // Problems by Topic
        List<Object[]> topicCounts = dsaProblemRepository.getProblemsCountByTopic(userId);
        Map<String, Integer> problemsByTopic = topicCounts.stream()
                .filter(arr -> arr[0] != null)
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> ((Long) arr[1]).intValue(),
                        (v1, v2) -> v1,
                        LinkedHashMap::new));
        analytics.setProblemsByTopic(problemsByTopic);

        // Goal Progress
        List<GoalProgress> goalProgress = activeGoals.stream()
                .limit(5)
                .map(g -> new GoalProgress(g.getTitle(), g.getCurrentValue(), g.getTargetValue()))
                .collect(Collectors.toList());
        analytics.setGoalProgress(goalProgress);

        return analytics;
    }
}
