# Analytics Dashboard Architecture Notes

This file collects the main architecture points for the Reporting & Analytics subsystem:

- what a design pattern is
- which design patterns we used
- how MVC applies here
- which GRASP principles are followed
- which SOLID principles are followed, and where

## 1. What Is A Design Pattern?

A design pattern is a reusable solution to a common software design problem.

It is not ready-made code. It is a proven design idea that helps keep code:

- easier to read
- easier to maintain
- easier to test
- easier to extend

## 2. Design Patterns Used In This Subsystem

### Facade

This is the main pattern to mention for the subsystem.

Why it fits:

- `DashboardService` gives one simple entry point for building the full dashboard.
- It hides the complexity of repositories, services, analytics engines, mapping, and reporting.

Main file:

- [`DashboardService.java`](../dashboard/DashboardService.java)

Supporting file:

- [`DashboardController.java`](../com/analytics/DashboardController.java)

### Adapter

This is used in the repository layer when data from the shared database JAR is converted into local domain models.

Why it fits:

- The database module returns its own models and records.
- Our repositories adapt that data into subsystem models like `SalesData`, `InventoryData`, and `ShipmentData`.

Main files:

- [`SalesRepository.java`](../repository/SalesRepository.java)
- [`InventoryRepository.java`](../repository/InventoryRepository.java)
- [`ShipmentRepository.java`](../repository/ShipmentRepository.java)
- [`ForecastRepository.java`](../repository/ForecastRepository.java)
- [`OrderRepository.java`](../repository/OrderRepository.java)
- [`WarehouseRepository.java`](../repository/WarehouseRepository.java)
- [`SupplierRepository.java`](../repository/SupplierRepository.java)

### DTO Pattern

We use DTOs to send clean data to the Swing client without exposing internal implementation objects.

Main files:

- [`DashboardDTO.java`](../dto/DashboardDTO.java)
- [`ReportDTO.java`](../dto/ReportDTO.java)
- [`KPIResult.java`](../dto/KPIResult.java)
- [`VisualizationDTO.java`](../dto/VisualizationDTO.java)

### Mapper

The mapper converts internal analytics objects into DTOs.

Main file:

- [`DashboardMapper.java`](../mapper/DashboardMapper.java)

### Repository Pattern

Repositories isolate database access from business logic.

Main files:

- [`SalesRepository.java`](../repository/SalesRepository.java)
- [`InventoryRepository.java`](../repository/InventoryRepository.java)
- [`OrderRepository.java`](../repository/OrderRepository.java)
- [`ShipmentRepository.java`](../repository/ShipmentRepository.java)
- [`WarehouseRepository.java`](../repository/WarehouseRepository.java)
- [`SupplierRepository.java`](../repository/SupplierRepository.java)
- [`ForecastRepository.java`](../repository/ForecastRepository.java)

## 3. MVC In This Subsystem

Yes, the subsystem follows MVC concepts, but in a desktop-client/back-end style.

### Model

The model side includes:

- domain classes
- DTOs
- internal analytics objects

Examples:

- [`SalesData.java`](../model/SalesData.java)
- [`ForecastData.java`](../model/ForecastData.java)
- [`OrderData.java`](../model/OrderData.java)
- [`ShipmentData.java`](../model/ShipmentData.java)
- [`WarehouseData.java`](../model/WarehouseData.java)
- [`SupplierData.java`](../model/SupplierData.java)
- [`KPIResult.java`](../dto/KPIResult.java)
- [`DashboardDTO.java`](../dto/DashboardDTO.java)

### View

The view is the Java Swing UI.

Main file:

- [`SwingDashboardApp.java`](../desktop/SwingDashboardApp.java)

### Controller

The controller receives dashboard requests and returns dashboard data.

Main file:

- [`DashboardController.java`](../com/analytics/DashboardController.java)

### Important Note

This is not a pure textbook single-process MVC app.

It is better described as:

- Java controller layer as the request-handling controller
- Java Swing as the desktop view
- domain objects, DTOs, and analytics classes as the model side
- service and repository layers between controller and model

So in class, you can safely say:

- "Yes, MVC concepts are used."
- "The code is better described as MVC-inspired with extra service, repository, and mapper layers."

## 4. GRASP Principles In This Subsystem

GRASP stands for General Responsibility Assignment Software Patterns.

### Information Expert

Each class stores and processes the data it is responsible for.

Examples:

- repositories know how to fetch their own data
- services know how to clean their own data
- engines know how to calculate analytics

Useful files:

- [`SalesRepository.java`](../repository/SalesRepository.java)
- [`InventoryRepository.java`](../repository/InventoryRepository.java)
- [`SalesService.java`](../service/SalesService.java)
- [`AnalyticsEngine.java`](../engine/AnalyticsEngine.java)

### Creator

Classes create objects that belong to their workflow.

Examples:

- `DashboardService` creates the repositories, services, and engines needed to build the dashboard
- `DashboardMapper` creates DTOs from internal results

Useful files:

- [`DashboardService.java`](../dashboard/DashboardService.java)
- [`DashboardMapper.java`](../mapper/DashboardMapper.java)

### Controller

The controller pattern in GRASP assigns input handling to a dedicated class.

Here, the controller handles requests and delegates work to the dashboard service.

Useful file:

- [`DashboardController.java`](../com/analytics/DashboardController.java)

### Low Coupling

The subsystem keeps dependencies separated through interfaces and layers.

Examples:

- services depend on repository interfaces
- the controller depends on `DashboardService`
- the Swing UI depends on the backend services, not on the database directly

Useful files:

- [`repository/interfaces/`](../repository/interfaces)
- [`DashboardController.java`](../com/analytics/DashboardController.java)
- [`SwingDashboardApp.java`](../desktop/SwingDashboardApp.java)

### High Cohesion

Each class has one clear responsibility.

Examples:

- repositories only fetch data
- services only clean and validate
- engines only calculate results
- mapper only converts data

Useful files:

- [`SalesService.java`](../service/SalesService.java)
- [`ReportGenerator.java`](../engine/ReportGenerator.java)
- [`DashboardMapper.java`](../mapper/DashboardMapper.java)

### Pure Fabrication

Some helper classes exist to keep the design clean, even if they are not real business entities.

Examples:

- `RepositoryExceptionSupport` centralizes repository error handling
- `DashboardMapper` centralizes DTO conversion

Useful files:

- [`RepositoryExceptionSupport.java`](../repository/RepositoryExceptionSupport.java)
- [`DashboardMapper.java`](../mapper/DashboardMapper.java)

## 5. SOLID Principles In This Subsystem

### S - Single Responsibility Principle

This is mostly followed.

Each layer has a clear job:

- controller handles HTTP
- service cleans and validates
- engine computes analytics
- mapper converts internal objects to DTOs
- repository talks to the database

Useful files:

- [`DashboardController.java`](../com/analytics/DashboardController.java)
- [`SalesService.java`](../service/SalesService.java)
- [`AnalyticsEngine.java`](../engine/AnalyticsEngine.java)
- [`DashboardMapper.java`](../mapper/DashboardMapper.java)
- [`SalesRepository.java`](../repository/SalesRepository.java)

### O - Open/Closed Principle

This is partially followed.

Why:

- new analytics rules can be added by extending engine logic
- new repositories or DTO fields can be added with limited impact

Useful files:

- [`AnalyticsEngine.java`](../engine/AnalyticsEngine.java)
- [`InsightAggregator.java`](../engine/InsightAggregator.java)
- [`ReportGenerator.java`](../engine/ReportGenerator.java)

Limitation:

- the export logic in [`ReportExportService.java`](../service/ReportExportService.java) uses format checks, so adding a new format would require code changes

### L - Liskov Substitution Principle

This is followed through repository interfaces.

Why:

- services depend on repository interfaces
- any compatible repository implementation can replace another one without breaking the service

Useful files:

- [`SalesService.java`](../service/SalesService.java)
- [`SalesRepositoryInterface.java`](../repository/interfaces/SalesRepositoryInterface.java)
- [`InventoryRepositoryInterface.java`](../repository/interfaces/InventoryRepositoryInterface.java)
- [`ForecastRepositoryInterface.java`](../repository/interfaces/ForecastRepositoryInterface.java)

### I - Interface Segregation Principle

This is followed.

Why:

- each repository has its own small interface
- we do not force one huge interface on every class

Useful files:

- [`repository/interfaces/`](../repository/interfaces)

### D - Dependency Inversion Principle

This is partially followed.

Why:

- service classes depend on interfaces, not concrete repository classes
- this helps testing and substitution

Useful files:

- [`SalesService.java`](../service/SalesService.java)
- [`InventoryService.java`](../service/InventoryService.java)
- [`ForecastService.java`](../service/ForecastService.java)

Limitation:

- [`DashboardService.java`](../dashboard/DashboardService.java) still creates many concrete objects directly, so dependency injection is not used everywhere

## 6. Easy Viva Answer

If you need a short answer in class, you can say:

"Our subsystem mainly uses the Facade pattern through `DashboardService`, and also uses Adapter in the repository layer to convert database JAR objects into local models. It follows MVC concepts in a Java Swing desktop-client style, and the GRASP principles are visible through Information Expert, Low Coupling, High Cohesion, and Controller. The SOLID principles are mostly followed through separate controller, service, repository, mapper, and engine layers."
