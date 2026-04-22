package service;

import dto.TableViewDTO;
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
import repository.interfaces.ForecastRepositoryInterface;
import repository.interfaces.InventoryRepositoryInterface;
import repository.interfaces.OrderRepositoryInterface;
import repository.interfaces.SalesRepositoryInterface;
import repository.interfaces.ShipmentRepositoryInterface;
import repository.interfaces.SupplierRepositoryInterface;
import repository.interfaces.WarehouseRepositoryInterface;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TableViewService {
    private final AnalyticsExceptionSource exceptionSource;

    public TableViewService() {
        this(new AnalyticsExceptionSource());
    }

    public TableViewService(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public TableViewDTO buildTableView(String datasetName) {
        String dataset = normalizeDataset(datasetName);
        return switch (dataset) {
            case "inventory" -> fromInventory();
            case "orders" -> fromOrders();
            case "shipments" -> fromShipments();
            case "warehouses" -> fromWarehouses();
            case "suppliers" -> fromSuppliers();
            case "forecasts" -> fromForecasts();
            default -> fromSales();
        };
    }

    public List<String> supportedDatasets() {
        return List.of("sales", "inventory", "orders", "shipments", "warehouses", "suppliers", "forecasts");
    }

    private String normalizeDataset(String datasetName) {
        String dataset = datasetName == null ? "sales" : datasetName.trim().toLowerCase(Locale.ROOT);
        return supportedDatasets().contains(dataset) ? dataset : "sales";
    }

    private TableViewDTO fromSales() {
        SalesRepositoryInterface repository = new SalesRepository(exceptionSource);
        List<SalesData> data = new SalesService(repository, exceptionSource).getCleanedData();
        List<String> columns = List.of("saleId", "productId", "quantitySold", "revenue", "saleDate");
        return new TableViewDTO("sales", columns, data.stream().map(item -> row(
                "saleId", item.getSaleId(),
                "productId", item.getProductId(),
                "quantitySold", item.getQuantitySold(),
                "revenue", item.getRevenue(),
                "saleDate", item.getSaleDate()
        )).toList(), LocalDateTime.now());
    }

    private TableViewDTO fromInventory() {
        InventoryRepositoryInterface repository = new InventoryRepository(exceptionSource);
        List<InventoryData> data = new InventoryService(repository, exceptionSource).getCleanedData();
        List<String> columns = List.of("productId", "productName", "quantity", "warehouseId", "unitCost");
        return new TableViewDTO("inventory", columns, data.stream().map(item -> row(
                "productId", item.getProductId(),
                "productName", item.getProductName(),
                "quantity", item.getQuantity(),
                "warehouseId", item.getWarehouseId(),
                "unitCost", item.getUnitCost()
        )).toList(), LocalDateTime.now());
    }

    private TableViewDTO fromOrders() {
        OrderRepositoryInterface repository = new OrderRepository(exceptionSource);
        List<OrderData> data = new OrderService(repository, exceptionSource).getCleanedData();
        List<String> columns = List.of("orderId", "customerId", "status", "totalAmount", "orderDate");
        return new TableViewDTO("orders", columns, data.stream().map(item -> row(
                "orderId", item.getOrderId(),
                "customerId", item.getCustomerId(),
                "status", item.getStatus(),
                "totalAmount", item.getTotalAmount(),
                "orderDate", item.getOrderDate()
        )).toList(), LocalDateTime.now());
    }

    private TableViewDTO fromShipments() {
        ShipmentRepositoryInterface repository = new ShipmentRepository(exceptionSource);
        List<ShipmentData> data = new ShipmentService(repository, exceptionSource).getCleanedData();
        List<String> columns = List.of("shipmentId", "orderId", "status", "dispatchDate", "deliveryDate", "delayed");
        return new TableViewDTO("shipments", columns, data.stream().map(item -> row(
                "shipmentId", item.getShipmentId(),
                "orderId", item.getOrderId(),
                "status", item.getStatus(),
                "dispatchDate", item.getDispatchDate(),
                "deliveryDate", item.getDeliveryDate(),
                "delayed", item.isDelayed()
        )).toList(), LocalDateTime.now());
    }

    private TableViewDTO fromWarehouses() {
        WarehouseRepositoryInterface repository = new WarehouseRepository(exceptionSource);
        List<WarehouseData> data = new WarehouseService(repository, exceptionSource).getCleanedData();
        List<String> columns = List.of("warehouseId", "location", "totalCapacity", "usedCapacity");
        return new TableViewDTO("warehouses", columns, data.stream().map(item -> row(
                "warehouseId", item.getWarehouseId(),
                "location", item.getLocation(),
                "totalCapacity", item.getTotalCapacity(),
                "usedCapacity", item.getUsedCapacity()
        )).toList(), LocalDateTime.now());
    }

    private TableViewDTO fromSuppliers() {
        SupplierRepositoryInterface repository = new SupplierRepository(exceptionSource);
        List<SupplierData> data = new SupplierService(repository, exceptionSource).getCleanedData();
        List<String> columns = List.of("supplierId", "supplierName", "region", "reliabilityScore");
        return new TableViewDTO("suppliers", columns, data.stream().map(item -> row(
                "supplierId", item.getSupplierId(),
                "supplierName", item.getSupplierName(),
                "region", item.getRegion(),
                "reliabilityScore", item.getReliabilityScore()
        )).toList(), LocalDateTime.now());
    }

    private TableViewDTO fromForecasts() {
        ForecastRepositoryInterface repository = new ForecastRepository(exceptionSource);
        List<ForecastData> data = new ForecastService(repository, exceptionSource).getCleanedData();
        List<String> columns = List.of("productId", "forecastedDemand", "actualDemand", "periodStart", "periodEnd");
        return new TableViewDTO("forecasts", columns, data.stream().map(item -> row(
                "productId", item.getProductId(),
                "forecastedDemand", item.getForecastedDemand(),
                "actualDemand", item.getActualDemand(),
                "periodStart", item.getPeriodStart(),
                "periodEnd", item.getPeriodEnd()
        )).toList(), LocalDateTime.now());
    }

    private Map<String, Object> row(Object... keyValues) {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            row.put(String.valueOf(keyValues[i]), formatValue(keyValues[i + 1]));
        }
        return row;
    }

    private Object formatValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime time) {
            return time.toString();
        }
        if (value instanceof java.time.LocalDate date) {
            return date.toString();
        }
        return value;
    }
}
