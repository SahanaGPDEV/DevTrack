package com.devtrack.service;

import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.model.Skill;
import com.devtrack.model.Skill.SkillCategory;
import com.devtrack.model.User;
import com.devtrack.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserService userService;

    public Skill addSkill(Long userId, Skill skill) {
        User user = userService.getUserById(userId);

        // Check for duplicate skill
        skillRepository.findByUserUserIdAndSkillName(userId, skill.getSkillName())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Skill already exists. Use update instead.");
                });

        skill.setUser(user);
        if (skill.getStartedDate() == null) {
            skill.setStartedDate(LocalDate.now());
        }
        return skillRepository.save(skill);
    }

    public List<Skill> getSkillsByUserId(Long userId) {
        return skillRepository.findByUserUserIdOrderByHoursPracticedDesc(userId);
    }

    public Skill getSkillById(Long skillId) {
        return skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillId));
    }

    public List<Skill> getSkillsByCategory(Long userId, SkillCategory category) {
        return skillRepository.findByUserUserIdAndCategory(userId, category);
    }

    public Skill updateSkill(Long skillId, Skill skillDetails) {
        Skill skill = getSkillById(skillId);

        if (skillDetails.getSkillName() != null)
            skill.setSkillName(skillDetails.getSkillName());
        if (skillDetails.getCategory() != null)
            skill.setCategory(skillDetails.getCategory());
        if (skillDetails.getProficiency() != null)
            skill.setProficiency(skillDetails.getProficiency());
        if (skillDetails.getHoursPracticed() != null)
            skill.setHoursPracticed(skillDetails.getHoursPracticed());
        if (skillDetails.getProjectsCount() != null)
            skill.setProjectsCount(skillDetails.getProjectsCount());
        if (skillDetails.getNotes() != null)
            skill.setNotes(skillDetails.getNotes());

        skill.setLastUsed(LocalDate.now());
        skill.updateProficiency();

        return skillRepository.save(skill);
    }

    public Skill addHoursToSkill(Long skillId, Double hours) {
        Skill skill = getSkillById(skillId);
        skill.addHours(hours);
        return skillRepository.save(skill);
    }

    public void deleteSkill(Long skillId) {
        Skill skill = getSkillById(skillId);
        skillRepository.delete(skill);
    }

    public Integer getTotalSkills(Long userId) {
        return skillRepository.countByUserUserId(userId);
    }
}
