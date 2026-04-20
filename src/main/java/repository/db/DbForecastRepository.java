package repository.db;

import model.ForecastData;
import repository.ForecastRepository;

import java.util.List;

public class DbForecastRepository implements ForecastRepository {
    @Override
    public List<ForecastData> fetchAll() {
        throw new UnsupportedOperationException("DB forecast repository is waiting for the external DB JAR integration.");
    }
}
