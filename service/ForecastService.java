package service;

import exception.AnalyticsExceptionSource;
import model.ForecastData;
import repository.ForecastRepository;

import java.util.ArrayList;
import java.util.List;

public class ForecastService {

    private static final int INVALID_DATA_ID = 2002;

    private final ForecastRepository repository;
    private final AnalyticsExceptionSource exceptionSource;

    public ForecastService(ForecastRepository repository, AnalyticsExceptionSource exceptionSource) {
        this.repository = repository;
        this.exceptionSource = exceptionSource;
    }

    public List<ForecastData> getCleanedData() {
        List<ForecastData> cleaned = new ArrayList<>();

        for (ForecastData forecastData : repository.fetchAll()) {
            if (forecastData == null) {
                exceptionSource.fireValidationFailure(INVALID_DATA_ID, "ForecastData", "null", "Record is null");
                continue;
            }
            if (isBlank(forecastData.getProductId())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "productId", null, "must not be blank");
                continue;
            }
            if (forecastData.getPeriodStart() == null || forecastData.getPeriodEnd() == null) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "forecastPeriod", forecastData.getProductId(), "start and end dates are required");
                continue;
            }
            if (forecastData.getPeriodEnd().isBefore(forecastData.getPeriodStart())) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "periodEnd", forecastData.getPeriodEnd().toString(), "must not be before periodStart");
                continue;
            }
            if (forecastData.getForecastedDemand() < 0) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "forecastedDemand", String.valueOf(forecastData.getForecastedDemand()), "must be non-negative");
                continue;
            }
            if (forecastData.getActualDemand() < 0) {
                exceptionSource.fireInvalidInput(INVALID_DATA_ID, "actualDemand", String.valueOf(forecastData.getActualDemand()), "must be non-negative");
                continue;
            }

            cleaned.add(forecastData);
        }

        return cleaned;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
