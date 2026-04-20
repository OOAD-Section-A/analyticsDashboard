package exception;

public class AnalyticsExceptionSource {
    public void logError(String message) {
        System.err.println("[AnalyticsException] " + message);
    }

    public RuntimeException createException(String message) {
        return new RuntimeException(message);
    }

    public void fireDataSourceUnavailable(String location, String message) {
        logError("Data source unavailable at " + location + ": " + message);
    }

    public void fireValidationFailure(int id, String entity, String field, String message) {
        logError(String.format("Validation failure [%d] %s.%s - %s", id, entity, field, message));
    }

    public void fireInvalidInput(int id, String field, String value, String message) {
        logError(String.format("Invalid input [%d] %s=%s - %s", id, field, value, message));
    }
}
