package repository.db;

import model.OrderData;
import repository.OrderRepository;

import java.util.List;

public class DbOrderRepository implements OrderRepository {
    @Override
    public List<OrderData> fetchAll() {
        throw new UnsupportedOperationException("DB order repository is waiting for the external DB JAR integration.");
    }
}
