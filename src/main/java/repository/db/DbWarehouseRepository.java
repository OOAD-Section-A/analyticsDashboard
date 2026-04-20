package repository.db;

import model.WarehouseData;
import repository.WarehouseRepository;

import java.util.List;

public class DbWarehouseRepository implements WarehouseRepository {
    @Override
    public List<WarehouseData> fetchAll() {
        throw new UnsupportedOperationException("DB warehouse repository is waiting for the external DB JAR integration.");
    }
}
