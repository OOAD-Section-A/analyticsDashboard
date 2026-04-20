package repository.mock;

import model.OrderData;
import repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;

public class MockOrderRepository implements OrderRepository {
    @Override
    public List<OrderData> fetchAll() {
        return List.of(
                new OrderData("O001", "C001", 23000.00, LocalDate.now().minusDays(5), "COMPLETED"),
                new OrderData("O002", "C002", 18700.00, LocalDate.now().minusDays(3), "PENDING"),
                new OrderData("O003", "C003", 7560.00, LocalDate.now().minusDays(2), "COMPLETED"),
                new OrderData("O004", "C004", 6450.00, LocalDate.now().minusDays(1), "PENDING")
        );
    }
}
