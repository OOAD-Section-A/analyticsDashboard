package repository.db;

import model.SalesData;
import repository.SalesRepository;

import java.util.List;

public class DbSalesRepository implements SalesRepository {
    @Override
    public List<SalesData> fetchAll() {
        throw new UnsupportedOperationException("DB sales repository is waiting for the external DB JAR integration.");
    }
}
