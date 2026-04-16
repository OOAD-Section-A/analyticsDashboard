package service;

import model.WarehouseData;
import repository.WarehouseRepository;

import java.util.List;
import java.util.stream.Collectors;

public class WarehouseService {

    private final WarehouseRepository repository;

    public WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
    }

    public List<WarehouseData> getCleanedData() {
        return repository.fetchAll().stream()
                .filter(w -> w != null)
                .filter(w -> w.getWarehouseId() != null && !w.getWarehouseId().isBlank())
                .filter(w -> w.getTotalCapacity() > 0)
                .collect(Collectors.toList());
    }
}