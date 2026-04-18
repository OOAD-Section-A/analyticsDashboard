package service;

import exception.AnalyticsExceptionSource;
import model.InventoryData;
import repository.InventoryRepository;

import java.util.ArrayList;
import java.util.List;

public class InventoryService {

    private static final int INVALID_DATA_ID = 2001;

    private final InventoryRepository repository;
    private final AnalyticsExceptionSource exceptionSource;

    public InventoryService(InventoryRepository repository, AnalyticsExceptionSource exceptionSource) {
        this.repository = repository;
        this.exceptionSource = exceptionSource;
    }

    public List<InventoryData> getCleanedData() {
        List<InventoryData> cleaned = new ArrayList<>();

        for (InventoryData inventoryData : repository.fetchAll()) {
            if (inventoryData == null) {
                exceptionSource.fireValidationFailure(INVALID_DATA_ID, "InventoryData", "null", "Record is null");
                continue;
            }
            if (isBlank(inventoryData.getProductId())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "productId", null, "must not be blank");
                continue;
            }
            if (isBlank(inventoryData.getProductName())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "productName", inventoryData.getProductId(), "must not be blank");
                continue;
            }
            if (isBlank(inventoryData.getWarehouseId())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "warehouseId", inventoryData.getProductId(), "must not be blank");
                continue;
            }
            if (inventoryData.getQuantity() < 0) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "quantity", String.valueOf(inventoryData.getQuantity()), "must be non-negative");
                continue;
            }
            if (inventoryData.getUnitCost() < 0) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "unitCost", String.valueOf(inventoryData.getUnitCost()), "must be non-negative");
                continue;
            }

            cleaned.add(inventoryData);
        }

        return cleaned;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
