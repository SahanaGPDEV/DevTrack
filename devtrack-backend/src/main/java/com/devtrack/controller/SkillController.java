package com.devtrack.controller;

import com.devtrack.dto.ApiResponse;
import com.devtrack.model.Skill;
import com.devtrack.model.Skill.SkillCategory;
import com.devtrack.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Skill>> addSkill(
            @PathVariable Long userId,
            @Valid @RequestBody Skill skill) {
        Skill createdSkill = skillService.addSkill(userId, skill);
        return ResponseEntity.ok(ApiResponse.success("Skill added", createdSkill));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Skill>>> getSkillsByUser(@PathVariable Long userId) {
        List<Skill> skills = skillService.getSkillsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @GetMapping("/{skillId}")
    public ResponseEntity<ApiResponse<Skill>> getSkillById(@PathVariable Long skillId) {
        Skill skill = skillService.getSkillById(skillId);
        return ResponseEntity.ok(ApiResponse.success(skill));
    }

    @GetMapping("/user/{userId}/category/{category}")
    public ResponseEntity<ApiResponse<List<Skill>>> getSkillsByCategory(
            @PathVariable Long userId,
            @PathVariable SkillCategory category) {
        List<Skill> skills = skillService.getSkillsByCategory(userId, category);
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<ApiResponse<Skill>> updateSkill(
            @PathVariable Long skillId,
            @RequestBody Skill skill) {
        Skill updatedSkill = skillService.updateSkill(skillId, skill);
        return ResponseEntity.ok(ApiResponse.success("Skill updated", updatedSkill));
    }

    @PutMapping("/{skillId}/hours")
    public ResponseEntity<ApiResponse<Skill>> addHours(
            @PathVariable Long skillId,
            @RequestParam Double hours) {
        Skill updatedSkill = skillService.addHoursToSkill(skillId, hours);
        return ResponseEntity.ok(ApiResponse.success("Hours added", updatedSkill));
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(@PathVariable Long skillId) {
        skillService.deleteSkill(skillId);
        return ResponseEntity.ok(ApiResponse.success("Skill deleted", null));
    }
}
