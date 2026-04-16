package com.jackfruit.scm.reporting.repository;

import com.jackfruit.scm.database.adapter.ReportingAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.reporting.model.ForecastData;

import java.util.List;
import java.util.stream.Collectors;

public class ForecastRepository {

    private final ReportingAdapter reportingAdapter;

    public ForecastRepository(SupplyChainDatabaseFacade facade) {
        this.reportingAdapter = new ReportingAdapter(facade);
    }

    public List<ForecastData> fetchAll() {
        return reportingAdapter.listForecasts().stream()
                .map(f -> new ForecastData(
                        f.productId(),
                        f.forecastedDemand(),
                        f.actualDemand(),
                        f.periodStart(),
                        f.periodEnd()
                ))
                .collect(Collectors.toList());
    }
}