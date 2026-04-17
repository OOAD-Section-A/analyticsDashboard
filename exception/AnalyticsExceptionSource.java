package exception;

public class AnalyticsExceptionSource {

    private static final String EVENT_TYPE = "ANALYTICS_EXCEPTION";
    private static final String SUBSYSTEM = "analyticsDashboard";
    private static final String SUMMARY = "Analytics subsystem event";

    private Object handler;

    public void registerHandler(Object handler) {
        this.handler = handler;
    }

    public void fireConnectionFailed(int id, String target, String detail) {
        raise(id, "MAJOR", target + " connection failed: " + safe(detail));
    }

    public void fireInvalidInput(int id, String field, String value, String rule) {
        raise(id, "MINOR", field + " invalid [" + safe(value) + "] - " + safe(rule));
    }

    public void fireValidationFailure(int id, String entity, String entityId, String reason) {
        raise(id, "MINOR", entity + " validation failed for [" + safe(entityId) + "]: " + safe(reason));
    }

    public void fireConfigurationError(int id, String key, String reason) {
        raise(id, "MAJOR", "Configuration error for [" + safe(key) + "]: " + safe(reason));
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
