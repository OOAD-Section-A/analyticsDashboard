package repository;

import model.OrderData;

import java.util.List;

public interface OrderRepository {
    List<OrderData> fetchAll();
}
