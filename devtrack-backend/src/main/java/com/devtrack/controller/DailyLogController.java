package com.devtrack.controller;

import com.devtrack.dto.ApiResponse;
import com.devtrack.model.DailyLog;
import com.devtrack.service.DailyLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class DailyLogController {

    @Autowired
    private DailyLogService dailyLogService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<DailyLog>> createLog(
            @PathVariable Long userId,
            @Valid @RequestBody DailyLog log) {
        DailyLog createdLog = dailyLogService.createLog(userId, log);
        return ResponseEntity.ok(ApiResponse.success("Log created", createdLog));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<DailyLog>>> getLogsByUser(@PathVariable Long userId) {
        List<DailyLog> logs = dailyLogService.getLogsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(logs));
    }

    @GetMapping("/{logId}")
    public ResponseEntity<ApiResponse<DailyLog>> getLogById(@PathVariable Long logId) {
        DailyLog log = dailyLogService.getLogById(logId);
        return ResponseEntity.ok(ApiResponse.success(log));
    }

    @GetMapping("/user/{userId}/range")
    public ResponseEntity<ApiResponse<List<DailyLog>>> getLogsInRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyLog> logs = dailyLogService.getLogsInDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(logs));
    }

    @PutMapping("/{logId}")
    public ResponseEntity<ApiResponse<DailyLog>> updateLog(
            @PathVariable Long logId,
            @RequestBody DailyLog log) {
        DailyLog updatedLog = dailyLogService.updateLog(logId, log);
        return ResponseEntity.ok(ApiResponse.success("Log updated", updatedLog));
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<ApiResponse<Void>> deleteLog(@PathVariable Long logId) {
        dailyLogService.deleteLog(logId);
        return ResponseEntity.ok(ApiResponse.success("Log deleted", null));
    }
}
