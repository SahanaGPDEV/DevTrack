package com.devtrack.service;

import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.model.DailyLog;
import com.devtrack.model.User;
import com.devtrack.repository.DailyLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DailyLogService {

    @Autowired
    private DailyLogRepository dailyLogRepository;

    @Autowired
    private UserService userService;

    public DailyLog createLog(Long userId, DailyLog log) {
        User user = userService.getUserById(userId);

        // Check for duplicate entry
        dailyLogRepository.findByUserUserIdAndLogDate(userId, log.getLogDate())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Log already exists for this date. Use update instead.");
                });

        log.setUser(user);
        DailyLog savedLog = dailyLogRepository.save(log);

        // Update user stats
        userService.addHours(userId, log.getHoursCoded());
        userService.updateStreak(userId, true);

        return savedLog;
    }

    public List<DailyLog> getLogsByUserId(Long userId) {
        return dailyLogRepository.findByUserUserIdOrderByLogDateDesc(userId);
    }

    public DailyLog getLogById(Long logId) {
        return dailyLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Log not found with id: " + logId));
    }

    public List<DailyLog> getLogsInDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return dailyLogRepository.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    public DailyLog updateLog(Long logId, DailyLog logDetails) {
        DailyLog log = getLogById(logId);

        if (logDetails.getHoursCoded() != null)
            log.setHoursCoded(logDetails.getHoursCoded());
        if (logDetails.getDescription() != null)
            log.setDescription(logDetails.getDescription());
        if (logDetails.getTechnologiesUsed() != null)
            log.setTechnologiesUsed(logDetails.getTechnologiesUsed());
        if (logDetails.getMood() != null)
            log.setMood(logDetails.getMood());
        if (logDetails.getProductivityScore() != null)
            log.setProductivityScore(logDetails.getProductivityScore());

        return dailyLogRepository.save(log);
    }

    public void deleteLog(Long logId) {
        DailyLog log = getLogById(logId);
        dailyLogRepository.delete(log);
    }

    public Double getTotalHoursInRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return dailyLogRepository.getTotalHoursInRange(userId, startDate, endDate);
    }

    public Double getAverageHoursPerDay(Long userId) {
        return dailyLogRepository.getAverageHoursPerDay(userId);
    }
}
