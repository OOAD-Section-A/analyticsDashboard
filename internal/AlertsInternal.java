package internal;

import java.util.List;

public class AlertsInternal {
    private final List<String> alerts;

    public AlertsInternal(List<String> alerts) {
        this.alerts = alerts;
    }

    public List<String> getAlerts() { return alerts; }
}