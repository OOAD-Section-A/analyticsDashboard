package com.jackfruit.scm.reporting.repository;

import com.jackfruit.scm.database.adapter.InventoryAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.reporting.model.SupplierData;

import java.util.List;
import java.util.stream.Collectors;

public class SupplierRepository {

    private final InventoryAdapter inventoryAdapter;

    public SupplierRepository(SupplyChainDatabaseFacade facade) {
        this.inventoryAdapter = new InventoryAdapter(facade);
    }

    public List<SupplierData> fetchAll() {
        return inventoryAdapter.listSuppliers().stream()
                .map(s -> new SupplierData(
                        s.supplierId(),
                        s.supplierName(),
                        s.region(),
                        s.reliabilityScore()
                ))
                .collect(Collectors.toList());
    }
}