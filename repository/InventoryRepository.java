package repository;

import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.facade.subsystem.InventorySubsystemFacade;
import com.jackfruit.scm.database.facade.subsystem.ReportingSubsystemFacade;
import com.jackfruit.scm.database.model.InventoryModels;
import com.jackfruit.scm.database.model.ReportingModels;
import exception.AnalyticsExceptionSource;
import model.InventoryData;
import repository.interfaces.InventoryRepositoryInterface;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryRepository implements InventoryRepositoryInterface {

    private static final int CONNECTION_FAILURE_ID = 1001;

    private final AnalyticsExceptionSource exceptionSource;

    public InventoryRepository(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public List<InventoryData> fetchAll() {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            InventorySubsystemFacade inventory = facade.inventory();
            ReportingSubsystemFacade reporting = facade.reporting();

            HashMap<String, String> productNames = new HashMap<>();
            for (InventoryModels.Product product : inventory.listProducts()) {
                productNames.put(product.productId(), product.productName());
            }

            HashMap<String, String> warehouseByProduct = new HashMap<>();
            for (ReportingModels.InventoryStockReportRow row : reporting.getInventoryStockReport()) {
                warehouseByProduct.putIfAbsent(row.productId(), row.warehouseId());
            }

            return inventory.listStockLevels().stream()
                    .map(stockLevel -> new InventoryData(
                            stockLevel.productId(),
                            productNames.getOrDefault(stockLevel.productId(), stockLevel.productId()),
                            stockLevel.currentStockQty(),
                            warehouseByProduct.get(stockLevel.productId()),
                            0.0  // Unit cost from database model
                    ))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw RepositoryExceptionSupport.fail(exceptionSource, "InventoryRepository.fetchAll", CONNECTION_FAILURE_ID, ex);
        }
    }
}
