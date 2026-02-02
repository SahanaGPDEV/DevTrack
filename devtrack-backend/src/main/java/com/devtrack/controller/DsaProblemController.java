package com.devtrack.controller;

import com.devtrack.dto.ApiResponse;
import com.devtrack.model.DsaProblem;
import com.devtrack.model.DsaProblem.Difficulty;
import com.devtrack.model.DsaProblem.Platform;
import com.devtrack.service.DsaProblemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dsa")
public class DsaProblemController {

    @Autowired
    private DsaProblemService dsaProblemService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<DsaProblem>> addProblem(
            @PathVariable Long userId,
            @Valid @RequestBody DsaProblem problem) {
        DsaProblem createdProblem = dsaProblemService.addProblem(userId, problem);
        return ResponseEntity.ok(ApiResponse.success("Problem added", createdProblem));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<DsaProblem>>> getProblemsByUser(@PathVariable Long userId) {
        List<DsaProblem> problems = dsaProblemService.getProblemsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(problems));
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<ApiResponse<DsaProblem>> getProblemById(@PathVariable Long problemId) {
        DsaProblem problem = dsaProblemService.getProblemById(problemId);
        return ResponseEntity.ok(ApiResponse.success(problem));
    }

    @GetMapping("/user/{userId}/difficulty/{difficulty}")
    public ResponseEntity<ApiResponse<List<DsaProblem>>> getProblemsByDifficulty(
            @PathVariable Long userId,
            @PathVariable Difficulty difficulty) {
        List<DsaProblem> problems = dsaProblemService.getProblemsByDifficulty(userId, difficulty);
        return ResponseEntity.ok(ApiResponse.success(problems));
    }

    @GetMapping("/user/{userId}/platform/{platform}")
    public ResponseEntity<ApiResponse<List<DsaProblem>>> getProblemsByPlatform(
            @PathVariable Long userId,
            @PathVariable Platform platform) {
        List<DsaProblem> problems = dsaProblemService.getProblemsByPlatform(userId, platform);
        return ResponseEntity.ok(ApiResponse.success(problems));
    }

    @GetMapping("/user/{userId}/topic")
    public ResponseEntity<ApiResponse<List<DsaProblem>>> getProblemsByTopic(
            @PathVariable Long userId,
            @RequestParam String topic) {
        List<DsaProblem> problems = dsaProblemService.getProblemsByTopic(userId, topic);
        return ResponseEntity.ok(ApiResponse.success(problems));
    }

    @PutMapping("/{problemId}")
    public ResponseEntity<ApiResponse<DsaProblem>> updateProblem(
            @PathVariable Long problemId,
            @RequestBody DsaProblem problem) {
        DsaProblem updatedProblem = dsaProblemService.updateProblem(problemId, problem);
        return ResponseEntity.ok(ApiResponse.success("Problem updated", updatedProblem));
    }

    @DeleteMapping("/{problemId}")
    public ResponseEntity<ApiResponse<Void>> deleteProblem(@PathVariable Long problemId) {
        dsaProblemService.deleteProblem(problemId);
        return ResponseEntity.ok(ApiResponse.success("Problem deleted", null));
    }
}
