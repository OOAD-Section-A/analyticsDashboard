package com.analytics;

import dashboard.DashboardService;
import dto.DashboardDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow CORS for demo
public class DashboardController {

    @GetMapping("/dashboard")
    public DashboardDTO getDashboard() {
        DashboardService dashboardService = new DashboardService();
        return dashboardService.buildDashboard();
    }
}