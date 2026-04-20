package repository.mock;

import model.ForecastData;
import repository.ForecastRepository;

import java.time.LocalDate;
import java.util.List;

public class MockForecastRepository implements ForecastRepository {
    @Override
    public List<ForecastData> fetchAll() {
        return List.of(
                new ForecastData("P001", 130, 118, LocalDate.now().minusDays(7), LocalDate.now().plusDays(7)),
                new ForecastData("P002", 95, 64, LocalDate.now().minusDays(3), LocalDate.now().plusDays(10)),
                new ForecastData("P003", 60, 42, LocalDate.now().minusDays(10), LocalDate.now().plusDays(20)),
                new ForecastData("P004", 45, 15, LocalDate.now().minusDays(2), LocalDate.now().plusDays(12))
        );
    }
}
