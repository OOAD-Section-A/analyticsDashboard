package dashboard;

import dto.DashboardDTO;
import dto.KPIResult;
import dto.ReportDTO;
import dto.VisualizationDTO;
import engine.AlertGenerator;
import engine.AnalyticsEngine;
import engine.InsightAggregator;
import engine.ReportGenerator;
import engine.VisualizationEngine;
import exception.AnalyticsExceptionSource;
import internal.KPIResultInternal;
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
import repository.interfaces.ForecastRepositoryInterface;
import repository.interfaces.InventoryRepositoryInterface;
import repository.interfaces.OrderRepositoryInterface;
import repository.interfaces.SalesRepositoryInterface;
import repository.interfaces.ShipmentRepositoryInterface;
import repository.interfaces.SupplierRepositoryInterface;
import repository.interfaces.WarehouseRepositoryInterface;
import service.ForecastService;
import service.InventoryService;
import service.OrderService;
import service.SalesService;
import service.ShipmentService;
import service.SupplierService;
import service.WarehouseService;

import java.util.List;

public class DashboardService implements DashboardProvider {

    private final AnalyticsExceptionSource exceptionSource;

    public DashboardService() {
        this(new AnalyticsExceptionSource());
    }

    public DashboardService(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public DashboardDTO buildDashboard() {
        InventoryRepositoryInterface inventoryRepository = new repository.InventoryRepository(exceptionSource);
        SalesRepositoryInterface salesRepository = new repository.SalesRepository(exceptionSource);
        OrderRepositoryInterface orderRepository = new repository.OrderRepository(exceptionSource);
        ShipmentRepositoryInterface shipmentRepository = new repository.ShipmentRepository(exceptionSource);
        WarehouseRepositoryInterface warehouseRepository = new repository.WarehouseRepository(exceptionSource);
        SupplierRepositoryInterface supplierRepository = new repository.SupplierRepository(exceptionSource);
        ForecastRepositoryInterface forecastRepository = new repository.ForecastRepository(exceptionSource);

        List<InventoryData> inventory = new InventoryService(inventoryRepository, exceptionSource).getCleanedData();
        List<SalesData> sales = new SalesService(salesRepository, exceptionSource).getCleanedData();
        List<OrderData> orders = new OrderService(orderRepository, exceptionSource).getCleanedData();
        List<ShipmentData> shipments = new ShipmentService(shipmentRepository, exceptionSource).getCleanedData();
        List<WarehouseData> warehouses = new WarehouseService(warehouseRepository, exceptionSource).getCleanedData();
        List<SupplierData> suppliers = new SupplierService(supplierRepository, exceptionSource).getCleanedData();
        List<ForecastData> forecasts = new ForecastService(forecastRepository, exceptionSource).getCleanedData();

        KPIResultInternal kpisInternal = new AnalyticsEngine()
                .compute(new AnalyticsInput(inventory, sales, orders, shipments, warehouses, suppliers, forecasts));

        List<String> insights = new InsightAggregator().generate(kpisInternal);
        List<String> alerts = new AlertGenerator().generate(new AlertInput(inventory, shipments));
        VisualizationDataInternal visualizationsInternal = new VisualizationEngine().buildCharts(new VisualizationInput(sales, inventory));
        ReportDataInternal reportInternal = new ReportGenerator().generate(kpisInternal, insights, alerts);

        DashboardMapper mapper = new DashboardMapper();
        KPIResult kpis = mapper.toKPIResult(kpisInternal);
        VisualizationDTO visualizations = mapper.toVisualizationDTO(visualizationsInternal);
        ReportDTO report = mapper.toReportDTO(reportInternal);

        return new DashboardDTO(kpis, insights, alerts, visualizations, report);
    }
}
