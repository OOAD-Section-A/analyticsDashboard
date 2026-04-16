package model;

import java.time.LocalDate;

public class ForecastData {
    private String productId;
    private double forecastedDemand;
    private double actualDemand;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    public ForecastData(String productId, double forecastedDemand, double actualDemand, LocalDate periodStart, LocalDate periodEnd) {
        this.productId = productId;
        this.forecastedDemand = forecastedDemand;
        this.actualDemand = actualDemand;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public String getProductId() { return productId; }
    public double getForecastedDemand() { return forecastedDemand; }
    public double getActualDemand() { return actualDemand; }
    public LocalDate getPeriodStart() { return periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
}