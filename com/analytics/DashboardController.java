package com.analytics;

import dashboard.DashboardService;
import dto.DashboardDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.ReportExportService;
import service.TableViewService;
import dto.TableViewDTO;

/**
 * REST API Controller for Analytics Dashboard
 * Handles HTTP requests and returns dashboard data as JSON
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DashboardController {
    private final DashboardService dashboardService = new DashboardService();
    private final ReportExportService reportExportService = new ReportExportService();
    private final TableViewService tableViewService = new TableViewService();

    /**
     * GET endpoint to retrieve complete dashboard data
     * 
     * @return DashboardDTO containing:
     *         - KPIs (14 key performance indicators)
     *         - Insights (business insights)
     *         - Alerts (system alerts)
     *         - Visualizations (revenue by product, inventory levels)
     *         - Report (summary and details)
     *         - Margin profitability results
     */
    @GetMapping("/dashboard")
    public DashboardDTO getDashboard() {
        return dashboardService.buildDashboard();
    }

    @GetMapping("/dashboard/report")
    public ResponseEntity<String> downloadReport(
            @RequestParam(defaultValue = "txt") String format,
            @RequestParam(required = false) String sections) {
        DashboardDTO dashboard = dashboardService.buildDashboard();
        ReportExportService.ExportedReport exported = reportExportService.export(dashboard, format, sections);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + exported.fileName() + "\"")
                .contentType(MediaType.parseMediaType(exported.mediaType()))
                .body(exported.content());
    }

    @GetMapping("/dashboard/table-view")
    public TableViewDTO getTableView(@RequestParam(defaultValue = "sales") String dataset) {
        return tableViewService.buildTableView(dataset);
    }
}
