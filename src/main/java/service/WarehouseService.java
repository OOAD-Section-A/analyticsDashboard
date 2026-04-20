package service;

import exception.AnalyticsExceptionSource;
import model.WarehouseData;
import repository.WarehouseRepository;

import java.util.ArrayList;
import java.util.List;

public class WarehouseService {

    private static final int INVALID_DATA_ID = 2007;

    private final WarehouseRepository repository;
    private final AnalyticsExceptionSource exceptionSource;

    public WarehouseService(WarehouseRepository repository, AnalyticsExceptionSource exceptionSource) {
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
            int usedCapacity = warehouseData.getUsedCapacity();
            if (usedCapacity < 0 || usedCapacity > warehouseData.getTotalCapacity()) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "usedCapacity", String.valueOf(usedCapacity), "must be between 0 and totalCapacity");
                continue;
            }

            cleaned.add(new WarehouseData(warehouseData.getWarehouseId(), warehouseData.getLocation(), warehouseData.getTotalCapacity(), usedCapacity));
        }

        return cleaned;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
