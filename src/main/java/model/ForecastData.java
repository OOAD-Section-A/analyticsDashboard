package model;

import java.time.LocalDate;

public class ForecastData {
    private String productId;
    private int forecastedDemand;
    private int actualDemand;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    public ForecastData() {}

    public ForecastData(String productId, int forecastedDemand, int actualDemand, LocalDate periodStart, LocalDate periodEnd) {
        this.productId = productId;
        this.forecastedDemand = forecastedDemand;
        this.actualDemand = actualDemand;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getForecastedDemand() {
        return forecastedDemand;
    }

    public void setForecastedDemand(int forecastedDemand) {
        this.forecastedDemand = forecastedDemand;
    }

    public int getActualDemand() {
        return actualDemand;
    }

    public void setActualDemand(int actualDemand) {
        this.actualDemand = actualDemand;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }
}
