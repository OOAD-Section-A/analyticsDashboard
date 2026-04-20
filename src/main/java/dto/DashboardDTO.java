package dto;

import java.util.List;

public class DashboardDTO {
    private final KPIResult kpis;
    private final List<String> insights;
    private final List<String> alerts;
    private final VisualizationDTO visualizations;
    private final ReportDTO report;
    private final MarginResultDTO marginResult;

    public DashboardDTO(KPIResult kpis, List<String> insights, List<String> alerts,
                        VisualizationDTO visualizations, ReportDTO report, 
                        MarginResultDTO marginResult) {
        this.kpis = kpis;
        this.insights = insights;
        this.alerts = alerts;
        this.visualizations = visualizations;
        this.report = report;
        this.marginResult = marginResult;
    }

    public KPIResult getKpis() { return kpis; }
    public List<String> getInsights() { return insights; }
    public List<String> getAlerts() { return alerts; }
    public VisualizationDTO getVisualizations() { return visualizations; }
    public ReportDTO getReport() { return report; }
    public MarginResultDTO getMarginResult() { return marginResult; }
}
