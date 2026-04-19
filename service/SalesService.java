package service;

import exception.AnalyticsExceptionSource;
import model.SalesData;
import repository.interfaces.SalesRepositoryInterface;

import java.util.ArrayList;
import java.util.List;

public class SalesService {

    private static final int INVALID_DATA_ID = 2004;

    private final SalesRepositoryInterface repository;
    private final AnalyticsExceptionSource exceptionSource;

    public SalesService(SalesRepositoryInterface repository, AnalyticsExceptionSource exceptionSource) {
        this.repository = repository;
        this.exceptionSource = exceptionSource;
    }

    public List<SalesData> getCleanedData() {
        List<SalesData> cleaned = new ArrayList<>();

        for (SalesData salesData : repository.fetchAll()) {
            if (salesData == null) {
                exceptionSource.fireValidationFailure(INVALID_DATA_ID, "SalesData", "null", "Record is null");
                continue;
            }
            if (isBlank(salesData.getSaleId())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "saleId", null, "must not be blank");
                continue;
            }
            if (isBlank(salesData.getProductId())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "productId", salesData.getSaleId(), "must not be blank");
                continue;
            }
            if (salesData.getSaleDate() == null) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "saleDate", salesData.getSaleId(), "must not be null");
                continue;
            }
            if (salesData.getQuantitySold() < 0) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "quantitySold", String.valueOf(salesData.getQuantitySold()), "must be non-negative");
                continue;
            }
            if (salesData.getRevenue() < 0) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "revenue", String.valueOf(salesData.getRevenue()), "must be non-negative");
                continue;
            }
            if (salesData.getQuantitySold() == 0 && salesData.getRevenue() > 0) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "revenue", salesData.getSaleId(), "must be zero when quantitySold is zero");
                continue;
            }

            cleaned.add(salesData);
        }

        return cleaned;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
