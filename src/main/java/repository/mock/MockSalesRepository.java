package repository.mock;

import model.SalesData;
import repository.SalesRepository;

import java.time.LocalDate;
import java.util.List;

public class MockSalesRepository implements SalesRepository {
    @Override
    public List<SalesData> fetchAll() {
        return List.of(
                new SalesData("S001", "P001", 92, 23000.00, LocalDate.now().minusDays(5)),
                new SalesData("S002", "P002", 68, 18700.00, LocalDate.now().minusDays(4)),
                new SalesData("S003", "P003", 42, 7560.00, LocalDate.now().minusDays(2)),
                new SalesData("S004", "P004", 15, 6450.00, LocalDate.now().minusDays(1))
        );
    }
}
