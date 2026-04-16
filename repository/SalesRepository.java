package com.jackfruit.scm.reporting.repository;

import com.jackfruit.scm.database.adapter.ReportingAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.reporting.model.SalesData;

import java.util.List;
import java.util.stream.Collectors;

public class SalesRepository {

    private final ReportingAdapter reportingAdapter;

    public SalesRepository(SupplyChainDatabaseFacade facade) {
        this.reportingAdapter = new ReportingAdapter(facade);
    }

    public List<SalesData> fetchAll() {
        return reportingAdapter.listSales().stream()
                .map(s -> new SalesData(
                        s.saleId(),
                        s.productId(),
                        s.quantitySold(),
                        s.revenue(),
                        s.saleDate()
                ))
                .collect(Collectors.toList());
    }
}