package service;

import model.ForecastData;
import repository.ForecastRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ForecastService {

    private final ForecastRepository repository;

    public ForecastService(ForecastRepository repository) {
        this.repository = repository;
    }

    public List<ForecastData> getCleanedData() {
        return repository.fetchAll().stream()
                .filter(f -> f != null)
                .filter(f -> f.getProductId() != null && !f.getProductId().isBlank())
                .filter(f -> f.getPeriodStart() != null && f.getPeriodEnd() != null)
                .collect(Collectors.toList());
    }
}