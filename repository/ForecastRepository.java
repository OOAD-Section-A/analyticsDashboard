package repository;

import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.model.DemandForecast;
import exception.AnalyticsExceptionSource;
import model.ForecastData;
import repository.interfaces.ForecastRepositoryInterface;

import java.util.List;
import java.util.stream.Collectors;

public class ForecastRepository implements ForecastRepositoryInterface {

    private static final int CONNECTION_FAILURE_ID = 1002;

    private final AnalyticsExceptionSource exceptionSource;

    public ForecastRepository(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public List<ForecastData> fetchAll() {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            return facade.demandForecasting().listForecasts().stream()
                    .map(this::mapForecast)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            exceptionSource.fireDataSourceUnavailable("ForecastRepository.fetchAll", ex.getMessage());
            throw new IllegalStateException("Failed to fetch forecast data", ex);
        }
    }

    private ForecastData mapForecast(DemandForecast forecast) {
        return new ForecastData(
                forecast.getProductId(),
                forecast.getPredictedDemand(),
                forecast.getActualDemand() != null ? forecast.getActualDemand() : 0,
                forecast.getForecastDate(),
                forecast.getForecastDate()
        );
    }
}
