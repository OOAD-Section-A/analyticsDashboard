# Mini Project Report Draft

Use this as paste-ready content for `Report_Template.docx`.
Replace the bracket placeholders before submission.

## Cover Page

Title:

Reporting and Analytics Dashboard for Supply Chain Management

Submitted by:

- Aneesh - [SRN]
- Akshat - [SRN]
- Akarsh T - [SRN]
- Anurag - [SRN]

Semester - Section:

[Semester] - [Section]

Faculty Name:

[Faculty Name]

## Problem Statement

The objective of this mini project is to design and implement a Reporting and Analytics subsystem for a Supply Chain Management system. The subsystem collects data from multiple operational areas such as sales, inventory, orders, shipments, warehouses, suppliers, and demand forecasts, then processes that data to generate KPIs, insights, alerts, tabular views, and downloadable reports. The system helps users monitor business performance, detect risks like delayed shipments and low inventory coverage, and review analytics through a Java Swing dashboard.

## Key Features

- Data integration from shared database module JARs
- Data cleaning and validation before analytics
- KPI calculation for revenue, orders, shipment performance, forecast accuracy, and inventory coverage
- Insights and alerts generation from analytics results
- Visualization support for demand trend and supplier performance
- Raw table data explorer with selectable datasets and columns
- Report export in TXT, CSV, and JSON
- Java Swing desktop dashboard for viewing subsystem outputs

## Models

The subsystem uses domain models to represent cleaned business data. The main models are:

- `SalesData` for sales transactions
- `InventoryData` for stock and inventory records
- `OrderData` for order-level information
- `ShipmentData` for logistics and delivery tracking
- `WarehouseData` for capacity and warehouse-related metrics
- `SupplierData` for supplier information and reliability
- `ForecastData` for forecasted and actual demand values

The subsystem also uses transfer and internal result models such as:

- `AnalyticsInput` to pass all cleaned datasets into the KPI engine
- `KPIResultInternal` to store computed KPI values
- `DashboardDTO`, `KPIResult`, `ReportDTO`, `VisualizationDTO`, and `TableViewDTO` to move processed data to the dashboard layer cleanly

## Use Case Diagram

The main actor is the user of the Reporting and Analytics subsystem.

Main use cases:

- View dashboard
- View KPI summary
- View insights and alerts
- View analytics charts
- Explore raw table data
- Select visible sections using filters
- Download report in selected format

Short explanation:

The user interacts with the Swing dashboard to inspect processed supply chain data. The system fetches data from repositories, validates it, computes KPIs, generates reports and visual summaries, and then displays the result in a desktop interface.

## Class Diagram

The subsystem follows a layered design.

Main class groups:

- Controller layer:
  - `DashboardController`
- Orchestration layer:
  - `DashboardService`
- Service layer:
  - `InventoryService`, `SalesService`, `OrderService`, `ShipmentService`, `WarehouseService`, `SupplierService`, `ForecastService`, `ReportExportService`, `TableViewService`
- Repository layer:
  - `InventoryRepository`, `SalesRepository`, `OrderRepository`, `ShipmentRepository`, `WarehouseRepository`, `SupplierRepository`, `ForecastRepository`
- Engine layer:
  - `AnalyticsEngine`, `AlertGenerator`, `InsightAggregator`, `ReportGenerator`, `VisualizationEngine`
- DTO and model layer:
  - `DashboardDTO`, `KPIResult`, `ReportDTO`, `VisualizationDTO`, `TableViewDTO`
  - `SalesData`, `InventoryData`, `OrderData`, `ShipmentData`, `WarehouseData`, `SupplierData`, `ForecastData`
- Desktop UI:
  - `SwingDashboardApp`

Short explanation:

Repositories fetch raw data, services clean and validate it, engines calculate KPIs and analytics, DTOs carry output data, and the Swing application displays the final dashboard to the user.

## State Diagram

Suggested state flow for the dashboard:

- Idle
- Loading Data
- Cleaning and Validation
- KPI and Analytics Computation
- Dashboard Displayed
- Export Requested
- Report Generated
- Error State

Short explanation:

When the user opens or refreshes the dashboard, the system moves from idle to data loading, then validates and processes the input, computes analytics, and finally displays the dashboard. If the user downloads a report, the subsystem enters the export flow. If invalid data or connection issues occur, the subsystem moves to an error state and reports the problem.

## Activity Diagrams

The subsystem can be represented using one integrated activity diagram.

### Integrated Activity Diagram

1. User launches the Swing dashboard
2. System loads raw data from repositories
3. Service layer cleans and validates the records
4. Analytics engine computes KPI values
5. Alert, insight, report, and visualization modules prepare outputs
6. Dashboard displays KPI cards, charts, alerts, insights, and tabular data
7. User optionally changes filters or selects another dataset
8. System refreshes the visible sections and table view
9. User optionally requests report download
10. Export service generates TXT, CSV, or JSON content
11. File chooser opens and the report is saved

Short explanation:

The integrated activity diagram captures the complete end-to-end workflow of the Reporting and Analytics subsystem. It begins when the user opens the Swing dashboard. The system then fetches raw data, validates and cleans it, computes KPIs, and prepares insights, alerts, reports, and visualizations. After the dashboard is displayed, the user can interact with filters, inspect table data, or download a report. This single integrated flow is clearer than separate isolated activity diagrams because it shows how all major subsystem functions are connected.

## Design Principles, and Design Patterns

## MVC Architecture Used? Yes/No

Yes.

The subsystem follows MVC concepts in a desktop-client style:

- Model:
  - domain models such as `SalesData`, `InventoryData`, `OrderData`, `ShipmentData`, `WarehouseData`, `SupplierData`, and `ForecastData`
  - result and transfer objects such as `KPIResult`, `DashboardDTO`, and `VisualizationDTO`
- View:
  - `SwingDashboardApp`
- Controller:
  - `DashboardController`

Although the subsystem also uses service, repository, and engine layers, the overall organization still reflects MVC principles.

## Design Principles

The subsystem applies important object-oriented design principles to keep the code maintainable and modular.

### SOLID Principles

Single Responsibility Principle:

Each class has one focused responsibility. Repository classes fetch data, service classes validate and clean data, engine classes compute analytics, DTOs transfer output data, and the Swing UI only handles presentation.

Open/Closed Principle:

The subsystem is partly open for extension. New KPIs, reports, or validation rules can be added by extending existing engines and services without redesigning the full system.

Liskov Substitution Principle:

Service classes depend on repository interfaces, so repository implementations can be replaced with test stubs or alternative implementations without breaking the service logic.

Interface Segregation Principle:

Separate repository interfaces are used for each data type, so classes depend only on the methods they actually need.

Dependency Inversion Principle:

High-level service logic depends on repository abstractions instead of concrete low-level database implementations.

### GRASP Principles

Information Expert:

Each class handles the responsibility that matches the data it owns. For example, cleaning services validate their own data type, and `AnalyticsEngine` calculates KPIs because it knows the KPI rules.

Creator:

Classes create related objects within their workflow. For example, `AnalyticsEngine` creates the KPI result object after computation, and `DashboardService` assembles the major subsystem parts.

Low Coupling:

The subsystem keeps the UI, services, repositories, and analytics engines separated so changes in one part have limited effect on others.

High Cohesion:

Each class does one closely related set of tasks, which makes the design easier to understand and test.

Pure Fabrication:

Classes such as service classes and mappers are created to keep business logic out of data models and repositories, even though those helper classes are not business entities themselves.

## Design Patterns

The main design patterns used in this project are:

Facade Pattern:

`DashboardService` acts as a facade by providing one simple entry point to build the entire dashboard, while hiding the internal complexity of repositories, services, analytics engines, reporting, and mapping.

Repository Pattern:

Repository classes isolate database access from business logic. This makes the service and engine layers independent from direct database code.

Adapter Pattern:

The repository layer adapts data from the shared database module JAR into the local subsystem model classes such as `SalesData`, `InventoryData`, and `ShipmentData`.

DTO Pattern:

DTO classes such as `DashboardDTO`, `KPIResult`, `ReportDTO`, and `VisualizationDTO` are used to transfer processed data cleanly between layers.

Mapper Pattern:

`DashboardMapper` converts internal subsystem results into DTOs that are easier to use in the dashboard flow.

## Github Link to the Codebase

[Paste public GitHub repository URL here]

## Screenshots

### UI

Insert screenshots of:

- main Swing dashboard
- KPI cards section
- analytics charts section
- data explorer table
- report download flow

## Individual Contributions of the Team Members

| Name | Module worked on |
|---|---|
| Aneesh | Data cleaning services, KPI engine, KPI validation self-test |
| Akshat | Database integration, repository-side connection flow, exception handling connection |
| Akarsh T | Cross-subsystem integration, `common.jar` usage, reporting and margin-related integration |
| Anurag | Swing dashboard presentation layer, visualization display, filters and table explorer UI |

## Aneesh Individual Contribution Writeup

Aneesh worked on the data cleaning and KPI computation part of the subsystem. This included implementing service-layer validation for inventory, sales, orders, shipments, suppliers, warehouses, and forecasts. The purpose of this layer is to remove invalid or inconsistent records before analytics is performed.

In addition, Aneesh implemented the KPI computation logic in `AnalyticsEngine`, where the subsystem calculates total revenue, total orders, pending orders, completed orders, delayed shipments, average warehouse utilization, average supplier reliability, revenue per order, inventory turnover ratio, forecast accuracy, on-time shipment rate, order completion rate, and inventory coverage days.

Aneesh also created `KpiCleaningSelfTest.java` to verify that invalid records are filtered correctly and that the KPI engine returns expected values from cleaned data. This helped validate the correctness of the analytics logic independently of the live database.

## Notes For Final Editing

Before submission, replace these placeholders:

- `[SRN]`
- `[Semester]`
- `[Section]`
- `[Faculty Name]`
- `[Paste public GitHub repository URL here]`

Also add:

- actual diagrams as images
- actual Swing dashboard screenshots
- team SRNs in the table
