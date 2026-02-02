package com.devtrack.repository;

import com.devtrack.model.Skill;
import com.devtrack.model.Skill.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByUserUserIdOrderByHoursPracticedDesc(Long userId);

    List<Skill> findByUserUserIdAndCategory(Long userId, SkillCategory category);

    Optional<Skill> findByUserUserIdAndSkillName(Long userId, String skillName);

    Integer countByUserUserId(Long userId);
}
