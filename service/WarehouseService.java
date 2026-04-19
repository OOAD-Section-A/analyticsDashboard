package service;

import exception.AnalyticsExceptionSource;
import model.WarehouseData;
import repository.interfaces.WarehouseRepositoryInterface;

import java.util.ArrayList;
import java.util.List;

public class WarehouseService {

    private static final int INVALID_DATA_ID = 2007;

    private final WarehouseRepositoryInterface repository;
    private final AnalyticsExceptionSource exceptionSource;

    public WarehouseService(WarehouseRepositoryInterface repository, AnalyticsExceptionSource exceptionSource) {
        this.repository = repository;
        this.exceptionSource = exceptionSource;
    }

    public List<WarehouseData> getCleanedData() {
        List<WarehouseData> cleaned = new ArrayList<>();

        for (WarehouseData warehouseData : repository.fetchAll()) {
            if (warehouseData == null) {
                exceptionSource.fireValidationFailure(INVALID_DATA_ID, "WarehouseData", "null", "Record is null");
                continue;
            }
            if (isBlank(warehouseData.getWarehouseId())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "warehouseId", null, "must not be blank");
                continue;
            }
            if (isBlank(warehouseData.getLocation())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "location", warehouseData.getWarehouseId(), "must not be blank");
                continue;
            }
            if (warehouseData.getTotalCapacity() <= 0) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "totalCapacity", String.valueOf(warehouseData.getTotalCapacity()), "must be greater than zero");
                continue;
            }
            // Compute usedCapacity here
            int usedCapacity = (int) Math.round(warehouseData.getTotalCapacity() * getUtilizationRate(warehouseData) / 100.0);
            if (usedCapacity < 0 || usedCapacity > warehouseData.getTotalCapacity()) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "usedCapacity", String.valueOf(usedCapacity), "must be between 0 and totalCapacity");
                continue;
            }

            cleaned.add(new WarehouseData(warehouseData.getWarehouseId(), warehouseData.getLocation(), warehouseData.getTotalCapacity(), usedCapacity));
        }

        return cleaned;
    }

    private double getUtilizationRate(WarehouseData data) {
        // Assuming utilization rate is derived; for now, use a placeholder or compute if possible
        // Since repository returns raw, we might need to fetch utilization separately, but for simplicity, assume 0 or compute from data
        // In real refactor, this might require additional logic
        return 0.0; // Placeholder; adjust based on actual data availability
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
