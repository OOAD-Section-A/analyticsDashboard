package dto;

import com.pricingos.common.MarginProfitabilityResult; // <-- NEW IMPORT
import java.util.List;

public class DashboardDTO {
    private final KPIResult kpis;
    private final List<String> insights;
    private final List<String> alerts;
    private final VisualizationDTO visualizations;
    private final ReportDTO report;
    private final MarginProfitabilityResult marginResult; // <-- NEW FIELD

    public DashboardDTO(KPIResult kpis, List<String> insights, List<String> alerts,
                        VisualizationDTO visualizations, ReportDTO report, 
                        MarginProfitabilityResult marginResult) { // <-- NEW PARAMETER
        this.kpis = kpis;
        this.insights = insights;
        this.alerts = alerts;
        this.visualizations = visualizations;
        this.report = report;
        this.marginResult = marginResult; // <-- NEW ASSIGNMENT
    }

    public KPIResult getKpis() { return kpis; }
    public List<String> getInsights() { return insights; }
    public List<String> getAlerts() { return alerts; }
    public VisualizationDTO getVisualizations() { return visualizations; }
    public ReportDTO getReport() { return report; }
    public MarginProfitabilityResult getMarginResult() { return marginResult; } // <-- NEW GETTER
}