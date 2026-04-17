package repository;

import com.jackfruit.scm.database.adapter.ReportingAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.model.ReportingModels;
import exception.AnalyticsExceptionSource;
import model.SupplierData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SupplierRepository {

    private static final int CONNECTION_FAILURE_ID = 1006;

    private final AnalyticsExceptionSource exceptionSource;

    public SupplierRepository(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public List<SupplierData> fetchAll() {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            ReportingAdapter reportingAdapter = new ReportingAdapter(facade);
            LinkedHashMap<String, SupplierData> suppliers = new LinkedHashMap<>();

            for (ReportingModels.DashboardReportRow row : reportingAdapter.getDashboardReport()) {
                if (row.supplierId() == null || row.supplierId().isBlank()) {
                    continue;
                }

                suppliers.putIfAbsent(
                        row.supplierId(),
                        new SupplierData(
                                row.supplierId(),
                                row.supplierName(),
                                "",
                                row.supplierPerformanceScore() == null ? 0.0 : row.supplierPerformanceScore()
                        )
                );
            }

            return new ArrayList<>(suppliers.values());
        } catch (Exception ex) {
            exceptionSource.fireConnectionFailed(CONNECTION_FAILURE_ID, "SupplierRepository.fetchAll", ex.getMessage());
            throw new IllegalStateException("Failed to fetch supplier data", ex);
        }
    }
}
