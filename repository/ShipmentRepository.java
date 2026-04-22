package repository;

import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.facade.subsystem.DeliveryOrdersSubsystemFacade;
import com.jackfruit.scm.database.model.Shipment;
import exception.AnalyticsExceptionSource;
import model.ShipmentData;
import repository.interfaces.ShipmentRepositoryInterface;

import java.util.List;
import java.util.stream.Collectors;

public class ShipmentRepository implements ShipmentRepositoryInterface {

    private static final int CONNECTION_FAILURE_ID = 1005;

    private final AnalyticsExceptionSource exceptionSource;

    public ShipmentRepository(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public List<ShipmentData> fetchAll() {
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            DeliveryOrdersSubsystemFacade deliveryOrders = facade.deliveryOrders();
            return deliveryOrders.listDeliveryOrders().stream()
                    .map(this::mapShipment)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw RepositoryExceptionSupport.fail(exceptionSource, "ShipmentRepository.fetchAll", CONNECTION_FAILURE_ID, ex);
        }
    }

    private ShipmentData mapShipment(Shipment shipment) {
        String status = shipment.getDeliveryStatus();
        return new ShipmentData(
                shipment.getDeliveryId(),
                shipment.getOrderId(),
                status,
                shipment.getCreatedAt() == null ? null : shipment.getCreatedAt().toLocalDate(),
                shipment.getDeliveryDate() == null ? null : shipment.getDeliveryDate().toLocalDate(),
                status != null && status.equalsIgnoreCase("DELAYED")
        );
    }
}
