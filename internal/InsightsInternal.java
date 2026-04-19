package internal;

import java.util.List;

public class InsightsInternal {
    private final List<String> insights;

    public InsightsInternal(List<String> insights) {
        this.insights = insights;
    }

    public List<String> getInsights() { return insights; }
}