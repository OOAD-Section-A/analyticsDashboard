package repository;

import com.jackfruit.scm.database.adapter.LogisticsAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.reporting.model.ShipmentData;

import java.util.List;
import java.util.stream.Collectors;

public class ShipmentRepository {

    private final LogisticsAdapter logisticsAdapter;

    public ShipmentRepository(SupplyChainDatabaseFacade facade) {
        this.logisticsAdapter = new LogisticsAdapter(facade);
    }

    public List<ShipmentData> fetchAll() {
        return logisticsAdapter.listShipments().stream()
                .map(s -> new ShipmentData(
                        s.shipmentId(),
                        s.orderId(),
                        s.status(),
                        s.expectedDate(),
                        s.actualDate()
                ))
                .collect(Collectors.toList());
    }
}