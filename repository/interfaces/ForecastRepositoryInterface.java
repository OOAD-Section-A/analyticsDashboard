package repository.interfaces;

import model.ForecastData;
import java.util.List;

public interface ForecastRepositoryInterface {
    List<ForecastData> fetchAll();
}