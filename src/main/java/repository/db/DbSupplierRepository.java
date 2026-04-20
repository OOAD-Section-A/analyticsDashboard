package repository.db;

import model.SupplierData;
import repository.SupplierRepository;

import java.util.List;

public class DbSupplierRepository implements SupplierRepository {
    @Override
    public List<SupplierData> fetchAll() {
        throw new UnsupportedOperationException("DB supplier repository is waiting for the external DB JAR integration.");
    }
}
