package com.devtrack.repository;

import com.devtrack.model.DailyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    List<DailyLog> findByUserUserIdOrderByLogDateDesc(Long userId);

    Optional<DailyLog> findByUserUserIdAndLogDate(Long userId, LocalDate logDate);

    @Query("SELECT dl FROM DailyLog dl WHERE dl.user.userId = :userId " +
            "AND dl.logDate BETWEEN :startDate AND :endDate ORDER BY dl.logDate DESC")
    List<DailyLog> findByUserIdAndDateRange(@Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(dl.hoursCoded), 0) FROM DailyLog dl " +
            "WHERE dl.user.userId = :userId AND dl.logDate BETWEEN :startDate AND :endDate")
    Double getTotalHoursInRange(@Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(AVG(dl.hoursCoded), 0) FROM DailyLog dl WHERE dl.user.userId = :userId")
    Double getAverageHoursPerDay(@Param("userId") Long userId);

    @Query("SELECT COUNT(dl) FROM DailyLog dl WHERE dl.user.userId = :userId")
    Integer countByUserId(@Param("userId") Long userId);
}
