import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class DBFile {
    public static void main(String[] args) {
        AutoCloseable facadeCloser = null;

        try {
            AdapterContext context = createReportingAdapter();
            Object reportingAdapter = context.adapter();
            facadeCloser = context.facadeCloser();

            Method dashboardMethod = reportingAdapter.getClass().getMethod("getDashboardReport");
            Object result = dashboardMethod.invoke(reportingAdapter);

            if (result instanceof List<?> rows) {
                System.out.println("Dashboard rows fetched: " + rows.size());
                rows.stream().limit(5).forEach(System.out::println);
            } else {
                System.out.println("Unexpected getDashboardReport() result type: " + result);
            }
        } catch (NoClassDefFoundError e) {
            System.err.println("Database module is missing required runtime classes: " + e.getMessage());
            System.err.println("Ask DB team for the full updated JAR set (or standalone JAR that includes all required classes).");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            System.err.println("Database call failed: " + cause.getMessage());
            cause.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (facadeCloser != null) {
                try {
                    facadeCloser.close();
                } catch (Exception closeError) {
                    System.err.println("Failed to close facade: " + closeError.getMessage());
                }
            }
        }
    }

    private static AdapterContext createReportingAdapter() throws Exception {
        Class<?> adapterClass = Class.forName("com.jackfruit.scm.database.adapter.ReportingAdapter");

        // Preferred path for new DB module builds where adapter owns connection setup.
        try {
            Object adapter = adapterClass.getDeclaredConstructor().newInstance();
            return new AdapterContext(adapter, null);
        } catch (NoSuchMethodException ignored) {
            // Fall back to older facade-based adapter wiring.
        }

        Class<?> facadeClass = Class.forName("com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade");
        Object facade = facadeClass.getDeclaredConstructor().newInstance();
        Object adapter = adapterClass.getDeclaredConstructor(facadeClass).newInstance(facade);
        return new AdapterContext(adapter, (AutoCloseable) facade);
    }

    private record AdapterContext(Object adapter, AutoCloseable facadeCloser) {}
}
