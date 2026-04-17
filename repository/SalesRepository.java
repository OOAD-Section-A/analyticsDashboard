package repository;

import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.model.DemandForecastingModels;
import exception.AnalyticsExceptionSource;
import model.SalesData;

import java.util.List;
import java.util.stream.Collectors;

public class SalesRepository {

    private static final int CONNECTION_FAILURE_ID = 1004;

    private final AnalyticsExceptionSource exceptionSource;

    public SalesRepository(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public List<SalesData> fetchAll() {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            return facade.demandForecasting().listSalesRecords().stream()
                    .map(this::mapSale)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            exceptionSource.fireConnectionFailed(CONNECTION_FAILURE_ID, "SalesRepository.fetchAll", ex.getMessage());
            throw new IllegalStateException("Failed to fetch sales data", ex);
        }
    }

    private SalesData mapSale(DemandForecastingModels.SalesRecord sale) {
        return new SalesData(
                sale.saleId(),
                sale.productId(),
                sale.quantitySold(),
                sale.revenue() == null ? 0.0 : sale.revenue().doubleValue(),
                sale.saleDate()
        );
    }
}
