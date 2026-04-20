package repository.mock;

import model.ShipmentData;
import repository.ShipmentRepository;

import java.time.LocalDate;
import java.util.List;

public class MockShipmentRepository implements ShipmentRepository {
    @Override
    public List<ShipmentData> fetchAll() {
        return List.of(
                new ShipmentData("SH001", "O001", "DELIVERED", LocalDate.now().minusDays(6), LocalDate.now().minusDays(3)),
                new ShipmentData("SH002", "O002", "DELAYED", LocalDate.now().minusDays(5), null),
                new ShipmentData("SH003", "O003", "DELIVERED", LocalDate.now().minusDays(3), LocalDate.now().minusDays(1)),
                new ShipmentData("SH004", "O004", "IN_TRANSIT", LocalDate.now().minusDays(1), null)
        );
    }
}
