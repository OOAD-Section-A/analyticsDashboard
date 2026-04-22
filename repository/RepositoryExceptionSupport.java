package repository;

import exception.AnalyticsExceptionSource;

final class RepositoryExceptionSupport {

    private RepositoryExceptionSupport() {
    }

    static IllegalStateException fail(AnalyticsExceptionSource exceptionSource, String operation, int failureId, Exception ex) {
        String detail = describe(ex);
        if (exceptionSource != null) {
            exceptionSource.fireConnectionFailed(failureId, operation, detail);
        }
        return new IllegalStateException(operation + " failed: " + detail, ex);
    }

    static String describe(Throwable ex) {
        if (ex == null) {
            return "unknown error";
        }

        Throwable root = ex;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }

        String message = root.getMessage();
        if (message == null || message.isBlank()) {
            message = root.getClass().getSimpleName();
        }

        if (root == ex) {
            return ex.getClass().getSimpleName() + ": " + message;
        }

        return ex.getClass().getSimpleName() + ": " + message + " (root cause: " + root.getClass().getSimpleName() + ")";
    }
}
