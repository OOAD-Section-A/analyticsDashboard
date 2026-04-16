package service;

import model.OrderData;
import repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<OrderData> getCleanedData() {
        return repository.fetchAll().stream()
                .filter(o -> o != null)
                .filter(o -> o.getOrderId() != null && !o.getOrderId().isBlank())
                .filter(o -> o.getStatus() != null && !o.getStatus().isBlank())
                .filter(o -> o.getTotalAmount() >= 0)
                .collect(Collectors.toList());
    }
}