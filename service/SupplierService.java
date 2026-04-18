package service;

import exception.AnalyticsExceptionSource;
import model.SupplierData;
import repository.SupplierRepository;

import java.util.ArrayList;
import java.util.List;

public class SupplierService {

    private static final int INVALID_DATA_ID = 2006;

    private final SupplierRepository repository;
    private final AnalyticsExceptionSource exceptionSource;

    public SupplierService(SupplierRepository repository, AnalyticsExceptionSource exceptionSource) {
        this.repository = repository;
        this.exceptionSource = exceptionSource;
    }

    public List<SupplierData> getCleanedData() {
        List<SupplierData> cleaned = new ArrayList<>();

        for (SupplierData supplierData : repository.fetchAll()) {
            if (supplierData == null) {
                exceptionSource.fireValidationFailure(INVALID_DATA_ID, "SupplierData", "null", "Record is null");
                continue;
            }
            if (isBlank(supplierData.getSupplierId())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "supplierId", null, "must not be blank");
                continue;
            }
            if (isBlank(supplierData.getSupplierName())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "supplierName", supplierData.getSupplierId(), "must not be blank");
                continue;
            }
            if (supplierData.getReliabilityScore() < 0 || supplierData.getReliabilityScore() > 100) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "reliabilityScore", String.valueOf(supplierData.getReliabilityScore()), "must be between 0 and 100");
                continue;
            }

            cleaned.add(supplierData);
        }

        return cleaned;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
