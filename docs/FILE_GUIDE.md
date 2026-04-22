# File Guide

This guide explains the purpose of the important files and folders in this project.
It focuses on the source and support files used by the Swing dashboard. Generated folders
such as `target/` and the legacy ignored `ui/` folder are intentionally excluded.

## Root Files

- [`README.md`](../README.md) - Main project overview and run instructions.
- [`pom.xml`](../pom.xml) - Maven build file for the backend application.
- [`application.properties`](../application.properties) - Application runtime settings.
- [`database.properties`](../database.properties) - MySQL connection settings used by the database module.
- [`prompt.txt`](../prompt.txt) - Saved prompt/reference notes used during development.
- [`DBFile.java`](../DBFile.java) - Local helper used to test the database module JAR.
- [`KpiCleaningSelfTest.java`](../KpiCleaningSelfTest.java) - Local self-test for KPI cleaning and repository/service flow.

## Backend Entry Point

- [`com/analytics/AnalyticsDashboardApplication.java`](../com/analytics/AnalyticsDashboardApplication.java) - Main backend application entry point.
- [`com/analytics/DashboardController.java`](../com/analytics/DashboardController.java) - Controller that exposes the dashboard, report export, and table view endpoints.

## Dashboard Orchestration

- [`dashboard/DashboardProvider.java`](../dashboard/DashboardProvider.java) - Interface for dashboard-building classes.
- [`dashboard/DashboardService.java`](../dashboard/DashboardService.java) - Orchestrates repositories, services, analytics engines, report generation, and margin integration.

## Engine Layer

- [`engine/AnalyticsEngine.java`](../engine/AnalyticsEngine.java) - Computes KPIs from the cleaned data.
- [`engine/AlertGenerator.java`](../engine/AlertGenerator.java) - Builds alert messages from inventory and shipment data.
- [`engine/InsightAggregator.java`](../engine/InsightAggregator.java) - Produces business insights from KPI results.
- [`engine/ReportGenerator.java`](../engine/ReportGenerator.java) - Builds the report summary text used by the UI and export endpoint.
- [`engine/VisualizationEngine.java`](../engine/VisualizationEngine.java) - Prepares chart data such as revenue-by-product and inventory levels.

## DTO Layer

- [`dto/DashboardDTO.java`](../dto/DashboardDTO.java) - Main API response object for the dashboard.
- [`dto/KPIResult.java`](../dto/KPIResult.java) - Serializable KPI output for the UI.
- [`dto/ReportDTO.java`](../dto/ReportDTO.java) - Report summary payload.
- [`dto/VisualizationDTO.java`](../dto/VisualizationDTO.java) - Visualization payload for chart data.
- [`dto/TableViewDTO.java`](../dto/TableViewDTO.java) - Table explorer payload containing columns and rows.

## Domain Models

- [`model/InventoryData.java`](../model/InventoryData.java) - Cleaned inventory row model used by the analytics layer.
- [`model/SalesData.java`](../model/SalesData.java) - Sales row model.
- [`model/OrderData.java`](../model/OrderData.java) - Order row model.
- [`model/ShipmentData.java`](../model/ShipmentData.java) - Shipment row model.
- [`model/WarehouseData.java`](../model/WarehouseData.java) - Warehouse row model.
- [`model/SupplierData.java`](../model/SupplierData.java) - Supplier row model.
- [`model/ForecastData.java`](../model/ForecastData.java) - Forecast row model.

## Service Layer

- [`service/InventoryService.java`](../service/InventoryService.java) - Cleans inventory data before analytics.
- [`service/SalesService.java`](../service/SalesService.java) - Cleans sales data.
- [`service/OrderService.java`](../service/OrderService.java) - Cleans order data.
- [`service/ShipmentService.java`](../service/ShipmentService.java) - Cleans shipment data.
- [`service/WarehouseService.java`](../service/WarehouseService.java) - Cleans warehouse data.
- [`service/SupplierService.java`](../service/SupplierService.java) - Cleans supplier data.
- [`service/ForecastService.java`](../service/ForecastService.java) - Cleans forecast data.
- [`service/ReportExportService.java`](../service/ReportExportService.java) - Exports dashboard data as TXT, CSV, or JSON.
- [`service/TableViewService.java`](../service/TableViewService.java) - Builds raw table views for the data explorer.

## Repository Layer

- [`repository/InventoryRepository.java`](../repository/InventoryRepository.java) - Reads inventory data from the database module.
- [`repository/SalesRepository.java`](../repository/SalesRepository.java) - Reads sales records.
- [`repository/OrderRepository.java`](../repository/OrderRepository.java) - Reads orders.
- [`repository/ShipmentRepository.java`](../repository/ShipmentRepository.java) - Reads delivery orders.
- [`repository/WarehouseRepository.java`](../repository/WarehouseRepository.java) - Reads warehouse data and dashboard context.
- [`repository/SupplierRepository.java`](../repository/SupplierRepository.java) - Reads supplier data.
- [`repository/ForecastRepository.java`](../repository/ForecastRepository.java) - Reads forecasts.
- [`repository/RepositoryExceptionSupport.java`](../repository/RepositoryExceptionSupport.java) - Shared repository error handling helper.

### Repository Interfaces

- [`repository/interfaces/InventoryRepositoryInterface.java`](../repository/interfaces/InventoryRepositoryInterface.java)
- [`repository/interfaces/SalesRepositoryInterface.java`](../repository/interfaces/SalesRepositoryInterface.java)
- [`repository/interfaces/OrderRepositoryInterface.java`](../repository/interfaces/OrderRepositoryInterface.java)
- [`repository/interfaces/ShipmentRepositoryInterface.java`](../repository/interfaces/ShipmentRepositoryInterface.java)
- [`repository/interfaces/WarehouseRepositoryInterface.java`](../repository/interfaces/WarehouseRepositoryInterface.java)
- [`repository/interfaces/SupplierRepositoryInterface.java`](../repository/interfaces/SupplierRepositoryInterface.java)
- [`repository/interfaces/ForecastRepositoryInterface.java`](../repository/interfaces/ForecastRepositoryInterface.java)

These interfaces let the service layer depend on abstractions instead of concrete classes.

## Mapping Layer

- [`mapper/DashboardMapper.java`](../mapper/DashboardMapper.java) - Converts internal analytics objects into DTOs.

## Exception Handling

- [`exception/AnalyticsExceptionSource.java`](../exception/AnalyticsExceptionSource.java) - Central exception reporting and validation bridge.

## Common Margin Contract

- [`common/com/pricingos/common/IMarginProfitabilityService.java`](../common/com/pricingos/common/IMarginProfitabilityService.java) - Margin service contract.
- [`common/com/pricingos/common/IProfitabilityAnalyticsObserver.java`](../common/com/pricingos/common/IProfitabilityAnalyticsObserver.java) - Observer contract for profitability events.
- [`common/com/pricingos/common/MarginProfitabilityResult.java`](../common/com/pricingos/common/MarginProfitabilityResult.java) - Immutable margin result record.

## Custom Margin Implementation

- [`com/pricingos/reporting/MarginProfitabilityServiceImpl.java`](../com/pricingos/reporting/MarginProfitabilityServiceImpl.java) - Local implementation that queries profitability data for margin conceded and protected.

## Desktop UI

- [`desktop/SwingDashboardApp.java`](../desktop/SwingDashboardApp.java) - Java Swing desktop dashboard that loads KPI, analytics, report, margin, and table data directly from the backend services.

## Database Support Files

- [`sql/seed-local-demo.sql`](../sql/seed-local-demo.sql) - Local demo data seed for the current OOAD schema.

## Library JARs

- [`lib/database-module-1.0.0-SNAPSHOT-standalone.jar`](../lib/database-module-1.0.0-SNAPSHOT-standalone.jar) - Shared database module used by the repositories and facades.
- [`lib/scm-exception-handler-v3.jar`](../lib/scm-exception-handler-v3.jar) - Exception handler JAR required by the shared database module.
- [`lib/common.jar`](../lib/common.jar) - Shared common types used by the margin integration.

## Documentation Folder

- [`docs/ARCHITECTURE_NOTES.md`](ARCHITECTURE_NOTES.md) - Design patterns, SOLID, and MVC notes.
- [`docs/INTEGRATION.md`](INTEGRATION.md) - Database module integration guide.
- [`docs/INTEGRATION_COMPLETE.md`](INTEGRATION_COMPLETE.md) - Integration summary.
- [`docs/PROJECT_VALIDATION_REPORT.md`](PROJECT_VALIDATION_REPORT.md) - Validation and verification notes.
- [`docs/QUICK_START.md`](QUICK_START.md) - Short run guide.
- [`docs/RUN_GUIDE.md`](RUN_GUIDE.md) - Detailed execution guide.
- [`docs/RUN_NOW.md`](RUN_NOW.md) - Quick start instructions.
- [`docs/START_HERE.md`](START_HERE.md) - First file to read for the project overview.
- [`docs/UI_INTEGRATION_PLAN.md`](UI_INTEGRATION_PLAN.md) - UI integration notes and decisions.
- [`docs/FILE_MANIFEST.md`](FILE_MANIFEST.md) - Legacy file manifest with historical setup notes.

## Notes

- The source root uses a flat layout instead of standard `src/main/java`.
- The legacy `ui/` folder is ignored and is not part of the current runtime path.
- Generated build output is not documented here because it changes every build.
- If you add a new backend or desktop file, update this guide so the submission stays current.
