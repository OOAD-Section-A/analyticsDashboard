package internal;

import java.time.LocalDateTime;
import java.util.List;

public class ReportDataInternal {
    private final String summary;
    private final List<String> insights;
    private final List<String> alerts;
    private final LocalDateTime generatedAt;

    public ReportDataInternal(String summary, List<String> insights, List<String> alerts, LocalDateTime generatedAt) {
        this.summary = summary;
        this.insights = insights;
        this.alerts = alerts;
        this.generatedAt = generatedAt;
    }

    public String getSummary() { return summary; }
    public List<String> getInsights() { return insights; }
    public List<String> getAlerts() { return alerts; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
}