package repository;

import com.jackfruit.scm.database.adapter.ReportingAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.model.ReportingModels;
import com.jackfruit.scm.database.model.Warehouse;
import exception.AnalyticsExceptionSource;
import model.WarehouseData;
import repository.interfaces.WarehouseRepositoryInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class WarehouseRepository implements WarehouseRepositoryInterface {

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
                String warehouseName = facade.getWarehouse(row.warehouseId())
                        .map(Warehouse::getWarehouseName)
                        .orElse(row.warehouseId());

                warehouses.putIfAbsent(
                        row.warehouseId(),
                        new WarehouseData(row.warehouseId(), warehouseName, totalCapacity, 0) // usedCapacity set to 0, computed in service
                );
            }

            return new ArrayList<>(warehouses.values());
        } catch (Exception ex) {
            exceptionSource.fireDataSourceUnavailable("WarehouseRepository.fetchAll", ex.getMessage());
            throw new IllegalStateException("Failed to fetch warehouse data", ex);
        }
    }
}
