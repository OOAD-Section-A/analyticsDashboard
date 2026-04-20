package repository.db;

import model.InventoryData;
import repository.InventoryRepository;

import java.util.List;

public class DbInventoryRepository implements InventoryRepository {
    @Override
    public List<InventoryData> fetchAll() {
        throw new UnsupportedOperationException("DB inventory repository is waiting for the external DB JAR integration.");
    }
}
