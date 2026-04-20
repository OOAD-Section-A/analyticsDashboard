package repository.mock;

import model.InventoryData;
import repository.InventoryRepository;

import java.util.List;

public class MockInventoryRepository implements InventoryRepository {
    @Override
    public List<InventoryData> fetchAll() {
        return List.of(
                new InventoryData("P001", "Widget Pro", "W001", 24, 125.00),
                new InventoryData("P002", "Gadget Mini", "W002", 6, 88.50),
                new InventoryData("P003", "Sprocket Pack", "W001", 18, 36.00),
                new InventoryData("P004", "Smart Sensor", "W003", 4, 210.00)
        );
    }
}
