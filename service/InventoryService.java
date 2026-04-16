package service;

import model.InventoryData;
import repository.InventoryRepository;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public List<InventoryData> getCleanedData() {
        return repository.fetchAll().stream()
                .filter(i -> i != null)
                .filter(i -> i.getProductId() != null && !i.getProductId().isBlank())
                .filter(i -> i.getQuantity() >= 0)
                .collect(Collectors.toList());
    }
}