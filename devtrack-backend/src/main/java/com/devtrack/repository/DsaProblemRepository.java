package com.devtrack.repository;

import com.devtrack.model.DsaProblem;
import com.devtrack.model.DsaProblem.Difficulty;
import com.devtrack.model.DsaProblem.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DsaProblemRepository extends JpaRepository<DsaProblem, Long> {

    List<DsaProblem> findByUserUserIdOrderBySolvedDateDesc(Long userId);

    List<DsaProblem> findByUserUserIdAndDifficulty(Long userId, Difficulty difficulty);

    List<DsaProblem> findByUserUserIdAndPlatform(Long userId, Platform platform);

    List<DsaProblem> findByUserUserIdAndTopicContainingIgnoreCase(Long userId, String topic);

    @Query("SELECT COUNT(p) FROM DsaProblem p WHERE p.user.userId = :userId AND p.difficulty = :difficulty")
    Integer countByUserIdAndDifficulty(@Param("userId") Long userId, @Param("difficulty") Difficulty difficulty);

    @Query("SELECT COUNT(p) FROM DsaProblem p WHERE p.user.userId = :userId " +
            "AND p.solvedDate BETWEEN :startDate AND :endDate")
    Integer countProblemsInRange(@Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT p.topic, COUNT(p) FROM DsaProblem p WHERE p.user.userId = :userId GROUP BY p.topic")
    List<Object[]> getProblemsCountByTopic(@Param("userId") Long userId);

    @Query("SELECT p.platform, COUNT(p) FROM DsaProblem p WHERE p.user.userId = :userId GROUP BY p.platform")
    List<Object[]> getProblemsCountByPlatform(@Param("userId") Long userId);

    Integer countByUserUserId(Long userId);
}
