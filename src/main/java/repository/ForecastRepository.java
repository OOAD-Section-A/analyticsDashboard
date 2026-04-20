package repository;

import model.ForecastData;

import java.util.List;

public interface ForecastRepository {
    List<ForecastData> fetchAll();
}
