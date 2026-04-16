package com.jackfruit.scm.reporting.service;

import com.jackfruit.scm.reporting.model.ShipmentData;
import com.jackfruit.scm.reporting.repository.ShipmentRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ShipmentService {

    private final ShipmentRepository repository;

    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    public List<ShipmentData> getCleanedData() {
        return repository.fetchAll().stream()
                .filter(s -> s != null)
                .filter(s -> s.getShipmentId() != null && !s.getShipmentId().isBlank())
                .filter(s -> s.getStatus() != null && !s.getStatus().isBlank())
                .collect(Collectors.toList());
    }
}