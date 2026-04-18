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
import model.ForecastData;
import model.InventoryData;
import model.OrderData;
import model.SalesData;
import model.ShipmentData;
import model.SupplierData;
import model.WarehouseData;
import repository.ForecastRepository;
import repository.InventoryRepository;
import repository.OrderRepository;
import repository.SalesRepository;
import repository.ShipmentRepository;
import repository.SupplierRepository;
import repository.WarehouseRepository;
import service.ForecastService;
import service.InventoryService;
import service.OrderService;
import service.SalesService;
import service.ShipmentService;
import service.SupplierService;
import service.WarehouseService;

import java.util.List;

public class DashboardService {

    private final AnalyticsExceptionSource exceptionSource;

    public DashboardService() {
        this(new AnalyticsExceptionSource());
    }

    public DashboardService(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public DashboardDTO buildDashboard() {
        InventoryRepository inventoryRepository = new InventoryRepository(exceptionSource);
        SalesRepository salesRepository = new SalesRepository(exceptionSource);
        OrderRepository orderRepository = new OrderRepository(exceptionSource);
        ShipmentRepository shipmentRepository = new ShipmentRepository(exceptionSource);
        WarehouseRepository warehouseRepository = new WarehouseRepository(exceptionSource);
        SupplierRepository supplierRepository = new SupplierRepository(exceptionSource);
        ForecastRepository forecastRepository = new ForecastRepository(exceptionSource);

        List<InventoryData> inventory = new InventoryService(inventoryRepository, exceptionSource).getCleanedData();
        List<SalesData> sales = new SalesService(salesRepository, exceptionSource).getCleanedData();
        List<OrderData> orders = new OrderService(orderRepository, exceptionSource).getCleanedData();
        List<ShipmentData> shipments = new ShipmentService(shipmentRepository, exceptionSource).getCleanedData();
        List<WarehouseData> warehouses = new WarehouseService(warehouseRepository, exceptionSource).getCleanedData();
        List<SupplierData> suppliers = new SupplierService(supplierRepository, exceptionSource).getCleanedData();
        List<ForecastData> forecasts = new ForecastService(forecastRepository, exceptionSource).getCleanedData();

        KPIResult kpis = new AnalyticsEngine()
                .compute(inventory, sales, orders, shipments, warehouses, suppliers, forecasts);

        List<String> insights = new InsightAggregator().generate(kpis);
        List<String> alerts = new AlertGenerator().generate(inventory, shipments);
        VisualizationDTO visualizations = new VisualizationEngine().buildCharts(sales, inventory);
        ReportDTO report = new ReportGenerator().generate(kpis, insights, alerts);

        return new DashboardDTO(kpis, insights, alerts, visualizations, report);
    }
}
