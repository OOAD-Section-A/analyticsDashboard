import com.jackfruit.scm.database.adapter.ReportingAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;

public class DBFile {
    public static void main(String[] args) {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            ReportingAdapter reportingAdapter = new ReportingAdapter(facade);
            boolean enableDashboardReport = false;

            runCheck("getInventoryStockReport", () -> reportingAdapter.getInventoryStockReport().size());
            runCheck("getPriceDiscountReport", () -> reportingAdapter.getPriceDiscountReport().size());
            runCheck("getExceptionReport", () -> reportingAdapter.getExceptionReport().size());
            runCheck("getCustomerTierCacheReport", () -> reportingAdapter.getCustomerTierCacheReport().size());

            if (enableDashboardReport) {
                runCheck("getDashboardReport", () -> reportingAdapter.getDashboardReport().size());
            } else {
                System.out.println("getDashboardReport SKIPPED due to DB module type-casting bug (Long -> Boolean)");
            }
        } catch (NoClassDefFoundError e) {
            System.err.println("Missing runtime class from DB module: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runCheck(String methodName, RowCountSupplier supplier) {
        try {
            int rows = supplier.get();
            System.out.println(methodName + " OK, rows=" + rows);
        } catch (Exception ex) {
            System.err.println(methodName + " FAILED: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FunctionalInterface
    private interface RowCountSupplier {
        int get() throws Exception;
    }
}
