package repository.db;

import model.ShipmentData;
import repository.ShipmentRepository;

import java.util.List;

public class DbShipmentRepository implements ShipmentRepository {
    @Override
    public List<ShipmentData> fetchAll() {
        throw new UnsupportedOperationException("DB shipment repository is waiting for the external DB JAR integration.");
    }
}
