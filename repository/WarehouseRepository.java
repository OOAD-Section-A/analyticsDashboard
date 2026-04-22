package repository;

import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.facade.subsystem.ReportingSubsystemFacade;
import com.jackfruit.scm.database.facade.subsystem.WarehouseSubsystemFacade;
import com.jackfruit.scm.database.model.ReportingModels;
import com.jackfruit.scm.database.model.Warehouse;
import exception.AnalyticsExceptionSource;
import model.WarehouseData;
import repository.interfaces.WarehouseRepositoryInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WarehouseRepository implements WarehouseRepositoryInterface {

    private static final int CONNECTION_FAILURE_ID = 1007;

    private final AnalyticsExceptionSource exceptionSource;

    public WarehouseRepository(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public List<WarehouseData> fetchAll() {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            WarehouseSubsystemFacade warehouseSubsystem = facade.warehouse();
            ReportingSubsystemFacade reporting = facade.reporting();

            Map<String, String> warehouseNames = warehouseSubsystem.listWarehouses().stream()
                    .collect(Collectors.toMap(
                            Warehouse::getWarehouseId,
                            Warehouse::getWarehouseName,
                            (left, right) -> left,
                            LinkedHashMap::new
                    ));

            LinkedHashMap<String, WarehouseData> warehouses = new LinkedHashMap<>();
            for (ReportingModels.DashboardReportRow row : reporting.getDashboardReport()) {
                if (row.warehouseId() == null || row.warehouseId().isBlank()) {
                    continue;
                }

                int totalCapacity = row.storageCapacity() == null ? 0 : row.storageCapacity();
                int usedCapacity = row.utilizationRate() == null || row.storageCapacity() == null
                        ? 0
                        : (int) Math.round((row.storageCapacity() * row.utilizationRate()) / 100.0);
                String warehouseName = warehouseNames.getOrDefault(row.warehouseId(), row.warehouseId());

                warehouses.putIfAbsent(
                        row.warehouseId(),
                        new WarehouseData(row.warehouseId(), warehouseName, totalCapacity, usedCapacity)
                );
            }

            return new ArrayList<>(warehouses.values());
        } catch (Exception ex) {
            throw RepositoryExceptionSupport.fail(exceptionSource, "WarehouseRepository.fetchAll", CONNECTION_FAILURE_ID, ex);
        }
    }
}
