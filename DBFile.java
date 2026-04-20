import com.jackfruit.scm.database.adapter.ReportingAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.model.ReportingModels;

import java.util.List;

public class DBFile {
    public static void main(String[] args) {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            ReportingAdapter reportingAdapter = new ReportingAdapter(facade);

            List<ReportingModels.DashboardReportRow> dashboardRows = reportingAdapter.getDashboardReport();
            List<ReportingModels.InventoryStockReportRow> inventoryRows = reportingAdapter.getInventoryStockReport();
            List<ReportingModels.PriceDiscountReportRow> discountRows = reportingAdapter.getPriceDiscountReport();
            List<ReportingModels.ExceptionReportRow> exceptionRows = reportingAdapter.getExceptionReport();
            List<ReportingModels.CustomerTierCacheRow> customerTierRows = reportingAdapter.getCustomerTierCacheReport();

            System.out.println("Dashboard rows: " + dashboardRows.size());
            System.out.println("Inventory stock rows: " + inventoryRows.size());
            System.out.println("Price discount rows: " + discountRows.size());
            System.out.println("Exception rows: " + exceptionRows.size());
            System.out.println("Customer tier cache rows: " + customerTierRows.size());

            dashboardRows.stream().limit(5).forEach(System.out::println);
        } catch (NoClassDefFoundError e) {
            System.err.println("Missing runtime class from DB module: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
