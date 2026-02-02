package com.devtrack.controller;

import com.devtrack.dto.AnalyticsDTO;
import com.devtrack.dto.ApiResponse;
import com.devtrack.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<AnalyticsDTO>> getAnalytics(@PathVariable Long userId) {
        AnalyticsDTO analytics = analyticsService.getAnalytics(userId);
        return ResponseEntity.ok(ApiResponse.success(analytics));
    }
}
