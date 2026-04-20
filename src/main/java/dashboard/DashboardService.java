package dashboard;

import dto.DashboardDTO;
import dto.KPIResult;
import dto.MarginResultDTO;
import dto.ReportDTO;
import dto.VisualizationDTO;
import engine.AlertGenerator;
import engine.AnalyticsEngine;
import engine.InsightAggregator;
import engine.ReportGenerator;
import engine.VisualizationEngine;
import exception.AnalyticsExceptionSource;
import internal.KPIResultInternal;
import internal.MarginResultInternal;
import internal.ReportDataInternal;
import internal.VisualizationDataInternal;
import internal.input.AnalyticsInput;
import internal.input.AlertInput;
import internal.input.VisualizationInput;
import mapper.DashboardMapper;
import model.ForecastData;
import model.InventoryData;
import model.OrderData;
import model.SalesData;
import model.ShipmentData;
import model.SupplierData;
import model.WarehouseData;
import repository.RepositoryProvider;
import service.ForecastService;
import service.InventoryService;
import service.OrderService;
import service.SalesService;
import service.ShipmentService;
import service.SupplierService;
import service.WarehouseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class DashboardService implements DashboardProvider {

    private final AnalyticsExceptionSource exceptionSource;
    private final RepositoryProvider repositoryProvider;

    public DashboardService() {
        this(new AnalyticsExceptionSource(), new RepositoryProvider());
    }

    public DashboardService(AnalyticsExceptionSource exceptionSource) {
        this(exceptionSource, new RepositoryProvider());
    }

    public DashboardService(AnalyticsExceptionSource exceptionSource, RepositoryProvider repositoryProvider) {
        this.exceptionSource = exceptionSource;
        this.repositoryProvider = repositoryProvider;
    }

    @Override
    public DashboardDTO buildDashboard() {
        List<InventoryData> inventory = new InventoryService(repositoryProvider.inventoryRepository(), exceptionSource).getCleanedData();
        List<SalesData> sales = new SalesService(repositoryProvider.salesRepository(), exceptionSource).getCleanedData();
        List<OrderData> orders = new OrderService(repositoryProvider.orderRepository(), exceptionSource).getCleanedData();
        List<ShipmentData> shipments = new ShipmentService(repositoryProvider.shipmentRepository(), exceptionSource).getCleanedData();
        List<WarehouseData> warehouses = new WarehouseService(repositoryProvider.warehouseRepository(), exceptionSource).getCleanedData();
        List<SupplierData> suppliers = new SupplierService(repositoryProvider.supplierRepository(), exceptionSource).getCleanedData();
        List<ForecastData> forecasts = new ForecastService(repositoryProvider.forecastRepository(), exceptionSource).getCleanedData();

        KPIResultInternal kpisInternal = new AnalyticsEngine()
                .compute(new AnalyticsInput(inventory, sales, orders, shipments, warehouses, suppliers, forecasts));

        List<String> insights = new InsightAggregator().generate(kpisInternal);
        List<String> alerts = new AlertGenerator().generate(new AlertInput(inventory, shipments));
        VisualizationDataInternal visualizationsInternal = new VisualizationEngine().buildCharts(new VisualizationInput(sales, inventory));
        MarginResultInternal marginResultInternal = fetchMarginResult(forecasts);
        ReportDataInternal reportInternal = new ReportGenerator().generate(kpisInternal, insights, alerts, marginResultInternal);

        DashboardMapper mapper = new DashboardMapper();
        KPIResult kpis = mapper.toKPIResult(kpisInternal);
        VisualizationDTO visualizations = mapper.toVisualizationDTO(visualizationsInternal);
        ReportDTO report = mapper.toReportDTO(reportInternal);
        MarginResultDTO marginResult = mapper.toMarginResultDTO(marginResultInternal);

        return new DashboardDTO(kpis, insights, alerts, visualizations, report, marginResult);
    }

    private MarginResultInternal fetchMarginResult(List<ForecastData> forecasts) {
        LocalDate startDate = forecasts.stream()
                .map(ForecastData::getPeriodStart)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(LocalDate.now());

        LocalDate endDate = forecasts.stream()
                .map(ForecastData::getPeriodEnd)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.now());

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return new MarginResultInternal(1250.00, 3840.00, startDateTime, endDateTime);
    }
}
