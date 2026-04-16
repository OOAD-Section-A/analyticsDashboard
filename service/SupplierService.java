package service;

import model.SupplierData;
import repository.SupplierRepository;

import java.util.List;
import java.util.stream.Collectors;

public class SupplierService {

    private final SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    public List<SupplierData> getCleanedData() {
        return repository.fetchAll().stream()
                .filter(s -> s != null)
                .filter(s -> s.getSupplierId() != null && !s.getSupplierId().isBlank())
                .filter(s -> s.getReliabilityScore() >= 0)
                .collect(Collectors.toList());
    }
}