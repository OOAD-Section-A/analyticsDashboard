package repository;

import com.jackfruit.scm.database.adapter.ReportingAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.model.ReportingModels;
import com.jackfruit.scm.database.model.Warehouse;
import exception.AnalyticsExceptionSource;
import model.WarehouseData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class WarehouseRepository {

    private static final int CONNECTION_FAILURE_ID = 1007;

    private final AnalyticsExceptionSource exceptionSource;

    public WarehouseRepository(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public List<WarehouseData> fetchAll() {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            ReportingAdapter reportingAdapter = new ReportingAdapter(facade);
            LinkedHashMap<String, WarehouseData> warehouses = new LinkedHashMap<>();

            for (ReportingModels.DashboardReportRow row : reportingAdapter.getDashboardReport()) {
                if (row.warehouseId() == null || row.warehouseId().isBlank()) {
                    continue;
                }

                int totalCapacity = row.storageCapacity() == null ? 0 : row.storageCapacity();
                double utilizationRate = row.utilizationRate() == null ? 0.0 : row.utilizationRate();
                int usedCapacity = (int) Math.round(totalCapacity * utilizationRate / 100.0);
                String warehouseName = facade.getWarehouse(row.warehouseId())
                        .map(Warehouse::getWarehouseName)
                        .orElse(row.warehouseId());

                warehouses.putIfAbsent(
                        row.warehouseId(),
                        new WarehouseData(row.warehouseId(), warehouseName, totalCapacity, usedCapacity)
                );
            }

            return new ArrayList<>(warehouses.values());
        } catch (Exception ex) {
            exceptionSource.fireConnectionFailed(CONNECTION_FAILURE_ID, "WarehouseRepository.fetchAll", ex.getMessage());
            throw new IllegalStateException("Failed to fetch warehouse data", ex);
        }
    }
}
