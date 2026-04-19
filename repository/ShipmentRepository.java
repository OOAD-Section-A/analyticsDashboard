package repository;

import com.jackfruit.scm.database.adapter.ReportingAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.model.ReportingModels;
import exception.AnalyticsExceptionSource;
import model.ShipmentData;
import repository.interfaces.ShipmentRepositoryInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShipmentRepository implements ShipmentRepositoryInterface {

    private static final int CONNECTION_FAILURE_ID = 1005;

    private final AnalyticsExceptionSource exceptionSource;

    public ShipmentRepository(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public List<ShipmentData> fetchAll() {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            ReportingAdapter reportingAdapter = new ReportingAdapter(facade);
            LinkedHashMap<String, ShipmentData> shipments = new LinkedHashMap<>();

            for (ReportingModels.DashboardReportRow row : reportingAdapter.getDashboardReport()) {
                if (row.shipmentId() == null || row.shipmentId().isBlank()) {
                    continue;
                }

                shipments.putIfAbsent(
                        row.shipmentId(),
                        new ShipmentData(
                                row.shipmentId(),
                                row.orderId(),
                                row.deliveryStatus(),
                                row.dispatchDate() == null ? null : row.dispatchDate().toLocalDate(),
                                row.deliveryDate() == null ? null : row.deliveryDate().toLocalDate(),
                                Boolean.TRUE.equals(row.delayFlag())
                        )
                );
            }

            return new ArrayList<>(shipments.values());
        } catch (Exception ex) {
            exceptionSource.fireDataSourceUnavailable("ShipmentRepository.fetchAll", ex.getMessage());
            throw new IllegalStateException("Failed to fetch shipment data", ex);
        }
    }
}
