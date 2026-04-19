package repository.interfaces;

import model.OrderData;
import java.util.List;

public interface OrderRepositoryInterface {
    List<OrderData> fetchAll();
}