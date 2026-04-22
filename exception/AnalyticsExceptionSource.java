package exception;

public class AnalyticsExceptionSource {

    public static final int UNREGISTERED_EXCEPTION = 0;
    public static final int INVALID_FILTER_INPUT = 15;
    public static final int DATA_SOURCE_UNAVAILABLE = 57;
    public static final int DB_AUTHENTICATION_FAILED = 251;
    public static final int VISUALIZATION_RENDER_ERROR = 354;
    public static final int REPORT_GENERATION_FAILED = 355;
    public static final int EXPORT_FORMAT_ERROR = 356;
    public static final int KPI_CALCULATION_ERROR = 357;
    public static final int ANALYTICS_PROCESSING_FAILED = 358;
    public static final int HIGH_RESPONSE_TIME = 364;

    private static final String EVENT_TYPE = "ANALYTICS_EXCEPTION";
    private static final String SUBSYSTEM = "reports dashboard";
    private static final String SUMMARY = "Reports dashboard subsystem event";

    private Object handler;

    public void registerHandler(Object handler) {
        this.handler = handler;
    }

    public void fireInvalidFilterInput(String field, String value, String rule) {
        raise(INVALID_FILTER_INPUT, "MINOR",
                "Report filter input invalid for [" + safe(field) + "]: " + safe(value) + " - " + safe(rule));
    }

    public void fireDataSourceUnavailable(String sourceName, String detail) {
        raise(DATA_SOURCE_UNAVAILABLE, "MAJOR",
                "Required data source unavailable [" + safe(sourceName) + "]: " + safe(detail));
    }

    public void fireDbAuthenticationFailed(String detail) {
        raise(DB_AUTHENTICATION_FAILED, "MAJOR",
                "Database authentication failed: " + safe(detail));
    }

    public void fireVisualizationRenderError(String chartName, String detail) {
        raise(VISUALIZATION_RENDER_ERROR, "MINOR",
                "Visualization render error for [" + safe(chartName) + "]: " + safe(detail));
    }

    public void fireReportGenerationFailed(String reportName, String detail) {
        raise(REPORT_GENERATION_FAILED, "MAJOR",
                "Report generation failed for [" + safe(reportName) + "]: " + safe(detail));
    }

    public void fireExportFormatError(String format, String detail) {
        raise(EXPORT_FORMAT_ERROR, "MINOR",
                "Export format error for [" + safe(format) + "]: " + safe(detail));
    }

    public void fireKpiCalculationError(String metricName, String detail) {
        raise(KPI_CALCULATION_ERROR, "MAJOR",
                "KPI calculation error for [" + safe(metricName) + "]: " + safe(detail));
    }

    public void fireAnalyticsProcessingFailed(String stage, String detail) {
        raise(ANALYTICS_PROCESSING_FAILED, "MAJOR",
                "Analytics processing failed at [" + safe(stage) + "]: " + safe(detail));
    }

    public void fireHighResponseTime(String operation, String detail) {
        raise(HIGH_RESPONSE_TIME, "WARNING",
                "High response time detected for [" + safe(operation) + "]: " + safe(detail));
    }

    public void fireUnregisteredException(String context, String detail) {
        raise(UNREGISTERED_EXCEPTION, "MINOR",
                "UNREGISTERED_EXCEPTION in [" + safe(context) + "]: " + safe(detail));
    }

    public void fireConnectionFailed(int id, String target, String detail) {
        raise(id, "MAJOR",
                "Connection failed for [" + safe(target) + "]: " + safe(detail));
    }

    public void fireInvalidInput(int id, String field, String value, String rule) {
        fireInvalidFilterInput(field, value, rule);
    }

    public void fireValidationFailure(int id, String entity, String entityId, String reason) {
        raise(INVALID_FILTER_INPUT, "MINOR",
                safe(entity) + " validation failed for [" + safe(entityId) + "]: " + safe(reason));
    }

    public void fireConfigurationError(int id, String key, String reason) {
        fireAnalyticsProcessingFailed(key, "Configuration error: " + safe(reason));
    }

    private void raise(int id, String severityName, String detail) {
        if (handler == null) {
            return;
        }

        try {
            Class<?> severityClass = Class.forName("com.scm.exceptions.Severity");
            Class<?> eventClass = Class.forName("com.scm.exceptions.SCMExceptionEvent");
            Object severity = Enum.valueOf(severityClass.asSubclass(Enum.class), severityName);
            Object event = eventClass
                    .getConstructor(int.class, String.class, severityClass, String.class, String.class, String.class)
                    .newInstance(id, EVENT_TYPE, severity, SUBSYSTEM, SUMMARY, detail);

            handler.getClass().getMethod("handle", eventClass).invoke(handler, event);
        } catch (ReflectiveOperationException ignored) {
            // Keep analytics flow alive even when the exception foundation JAR is unavailable.
        }
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "n/a" : value;
    }
}
