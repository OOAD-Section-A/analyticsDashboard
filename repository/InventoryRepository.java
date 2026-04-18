package repository;

import com.jackfruit.scm.database.adapter.InventoryAdapter;
import com.jackfruit.scm.database.adapter.ReportingAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.model.InventoryModels;
import com.jackfruit.scm.database.model.ReportingModels;
import exception.AnalyticsExceptionSource;
import model.InventoryData;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryRepository {

    private static final int CONNECTION_FAILURE_ID = 1001;

    private final AnalyticsExceptionSource exceptionSource;

    public InventoryRepository(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public List<InventoryData> fetchAll() {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            InventoryAdapter inventoryAdapter = new InventoryAdapter(facade);
            ReportingAdapter reportingAdapter = new ReportingAdapter(facade);

            HashMap<String, String> productNames = new HashMap<>();
            for (InventoryModels.Product product : inventoryAdapter.listProducts()) {
                productNames.put(product.productId(), product.productName());
            }

            HashMap<String, String> warehouseByProduct = new HashMap<>();
            for (ReportingModels.InventoryStockReportRow row : reportingAdapter.getInventoryStockReport()) {
                warehouseByProduct.putIfAbsent(row.productId(), row.warehouseId());
            }

            return inventoryAdapter.listStockLevels().stream()
                    .map(stockLevel -> new InventoryData(
                            stockLevel.productId(),
                            productNames.getOrDefault(stockLevel.productId(), stockLevel.productId()),
                            stockLevel.currentStockQty(),
                            warehouseByProduct.get(stockLevel.productId()),
                            0.0
                    ))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            exceptionSource.fireDataSourceUnavailable("InventoryRepository.fetchAll", ex.getMessage());
            throw new IllegalStateException("Failed to fetch inventory data", ex);
        }
    }
}
