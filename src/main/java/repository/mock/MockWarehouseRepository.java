package repository.mock;

import model.WarehouseData;
import repository.WarehouseRepository;

import java.util.List;

public class MockWarehouseRepository implements WarehouseRepository {
    @Override
    public List<WarehouseData> fetchAll() {
        return List.of(
                new WarehouseData("W001", "Dallas Warehouse", 500, 462),
                new WarehouseData("W002", "Chicago Warehouse", 350, 331),
                new WarehouseData("W003", "Austin Warehouse", 420, 286)
        );
    }
}
