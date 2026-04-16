package com.jackfruit.scm.reporting.service;

import com.jackfruit.scm.reporting.model.SalesData;
import com.jackfruit.scm.reporting.repository.SalesRepository;

import java.util.List;
import java.util.stream.Collectors;

public class SalesService {

    private final SalesRepository repository;

    public SalesService(SalesRepository repository) {
        this.repository = repository;
    }

    public List<SalesData> getCleanedData() {
        return repository.fetchAll().stream()
                .filter(s -> s != null)
                .filter(s -> s.getSaleId() != null && !s.getSaleId().isBlank())
                .filter(s -> s.getRevenue() >= 0)
                .filter(s -> s.getSaleDate() != null)
                .collect(Collectors.toList());
    }
}