# Aneesh Part Notes

This note is for the part assigned to Aneesh:

- data cleaning services
- KPI computation engine
- self-test for cleaning and KPI logic

Main files in this part:

- `service/InventoryService.java`
- `service/SalesService.java`
- `service/ForecastService.java`
- `service/OrderService.java`
- `service/ShipmentService.java`
- `service/SupplierService.java`
- `service/WarehouseService.java`
- `engine/AnalyticsEngine.java`
- `KpiCleaningSelfTest.java`

## 1. Big Picture

Your part takes raw data from repositories, removes bad records, and then calculates KPIs from the cleaned data.

The flow is:

1. repository returns raw records
2. service validates and filters them
3. `AnalyticsEngine` computes KPI values
4. self-test proves the cleaning and KPI rules work

## 2. File-by-File Explanation

### `InventoryService.java`

Purpose:

- gets all inventory rows from the repository
- removes invalid rows
- returns only clean inventory data

How it works:

- `INVALID_DATA_ID = 2001`
  - fixed code used when inventory validation fails
  - helps exception handling stay consistent
- `repository`
  - dependency used to fetch inventory records
- `exceptionSource`
  - dependency used to report validation problems
- constructor
  - receives the repository and exception handler from outside
  - this is good because the service does not create its own dependencies
- `getCleanedData()`
  - creates a new `cleaned` list
  - loops over every row from `repository.fetchAll()`
  - for each row:
    - rejects `null`
    - rejects blank `productId`
    - rejects blank `productName`
    - rejects blank `warehouseId`
    - rejects negative `quantity`
    - rejects negative `unitCost`
  - if a row passes every check, it is added to `cleaned`
- `isBlank()`
  - small helper to avoid repeating `null || isBlank()`

Why it matters:

- KPI logic should only use trusted data
- if you skip cleaning, KPIs become misleading

### `SalesService.java`

Purpose:

- validates sales rows before revenue and sales KPIs are calculated

How it works:

- `INVALID_DATA_ID = 2004`
  - code for sales validation errors
- constructor
  - injects repository and exception source
- `getCleanedData()`
  - rejects `null`
  - rejects blank `saleId`
  - rejects blank `productId`
  - rejects `null` `saleDate`
  - rejects negative `quantitySold`
  - rejects negative `revenue`
  - rejects the business-rule mismatch:
    - `quantitySold == 0` and `revenue > 0`
  - valid rows are added to the cleaned list

Important viva point:

- this class checks both technical validity and business validity
- example of business validity:
  - a sale with zero units but positive revenue is logically inconsistent

### `ForecastService.java`

Purpose:

- validates forecast rows used for forecast accuracy and inventory coverage

How it works:

- rejects `null`
- rejects blank `productId`
- rejects missing `periodStart` or `periodEnd`
- rejects `periodEnd` before `periodStart`
- rejects negative `forecastedDemand`
- rejects negative `actualDemand`

Important viva point:

- forecast accuracy only makes sense if forecast and actual values are valid and time periods are meaningful

### `OrderService.java`

Purpose:

- validates order rows before order KPIs are calculated

How it works:

- rejects `null`
- rejects blank `orderId`
- rejects blank `customerId`
- rejects blank `status`
- rejects `null` `orderDate`
- rejects negative `totalAmount`

Used for:

- total orders
- pending orders
- completed orders
- order completion rate

### `ShipmentService.java`

Purpose:

- validates shipment records before shipment KPIs are computed

How it works:

- rejects `null`
- rejects blank `shipmentId`
- rejects blank `orderId`
- rejects blank `status`
- rejects `null` `dispatchDate`
- rejects delivery dates earlier than dispatch dates

Used for:

- delayed shipments
- on-time shipment rate

Important viva point:

- `deliveryDate` before `dispatchDate` is not just bad data format, it is impossible business data

### `SupplierService.java`

Purpose:

- validates supplier records before supplier quality metrics are used

How it works:

- rejects `null`
- rejects blank `supplierId`
- rejects blank `supplierName`
- rejects `reliabilityScore < 0` or `> 100`

Used for:

- average supplier reliability KPI

### `WarehouseService.java`

Purpose:

- validates warehouse data before utilization KPIs are computed

How it works:

- rejects `null`
- rejects blank `warehouseId`
- rejects blank `location`
- rejects `totalCapacity <= 0`
- calculates `usedCapacity`
- rejects used capacity if it falls outside `0..totalCapacity`
- returns a normalized `WarehouseData` object

Important note:

- `getUtilizationRate()` currently returns `0.0` as a placeholder
- so this class is structured correctly, but the utilization derivation is still incomplete unless richer source data is provided

Best viva answer for that:

- "The class is responsible for validating warehouse capacity data and normalizing used capacity. The utilization-rate helper is currently a placeholder because the repository does not yet provide a complete utilization source."

### `AnalyticsEngine.java`

Purpose:

- central KPI calculator for the subsystem

How it works:

- `compute(AnalyticsInput input)`
  - receives one object containing all cleaned datasets
  - extracts:
    - inventory
    - sales
    - orders
    - shipments
    - warehouses
    - suppliers
    - forecasts
- calculates base totals:
  - `totalRevenue`
  - `totalOrders`
  - `pendingOrders`
  - `completedOrders`
  - `totalInventoryUnits`
  - `delayedShipments`
- calculates averages:
  - `avgWarehouseUtilization`
  - `avgSupplierReliability`
- calculates derived KPIs:
  - `totalUnitsSold`
  - `revenuePerOrder`
  - `inventoryTurnoverRatio`
  - `forecastAccuracyRate`
  - `onTimeShipmentRate`
  - `orderCompletionRate`
  - `averageDailyDemand`
  - `inventoryCoverageDays`
- returns one `KPIResultInternal` object containing all KPI values

Helper methods:

- `calculateForecastAccuracyRate()`
  - skips forecasts where demand is `<= 0`
  - computes:
    - `error = abs(forecast - actual)`
    - `accuracy = 1 - error / forecast`
  - clamps accuracy at minimum `0`
  - converts to percentage
  - returns average accuracy across comparable forecasts
- `isCompletedStatus()`
  - treats `COMPLETED`, `DELIVERED`, and `FULFILLED` as completed states
- `round()`
  - rounds values to 2 decimal places for cleaner display

Important viva points:

- this class does not fetch data and does not validate data
- it assumes the cleaning layer already did that
- this separation is deliberate and follows single responsibility

### `KpiCleaningSelfTest.java`

Purpose:

- local test harness for your part
- proves that cleaning removes bad rows and KPI computation returns expected answers

How it works:

- creates one shared `AnalyticsExceptionSource`
- creates each cleaning service using a stub repository instead of the real database
- calls `getCleanedData()` on every service
- checks the cleaned sizes
  - example:
    - inventory starts with 4 rows but only 2 remain
- creates `AnalyticsInput`
- runs `new AnalyticsEngine().compute(input)`
- checks every KPI with assertions
- prints success message if everything passes

Why it is useful:

- lets you prove business logic without needing MySQL or the real JARs
- isolates your part for easier debugging

## 3. Line-by-Line Viva Walkthrough For The Most Important Files

### `AnalyticsEngine.java`

- Lines 1-13:
  - package and imports
  - bring in input model, output model, and all domain datasets
- Line 15:
  - class declaration
- Line 17:
  - main public method `compute`
  - this is the main entry point of the KPI engine
- Lines 18-24:
  - unpack datasets from `AnalyticsInput`
  - this avoids passing 7 separate parameters
- Lines 25-38:
  - compute direct totals and counts from streams
- Lines 40-47:
  - compute averages for warehouses and suppliers
- Lines 49-61:
  - compute derived KPIs from previously calculated totals
- Lines 63-78:
  - package all KPI values into one immutable result object
- Lines 81-100:
  - dedicated method for forecast accuracy logic
  - extracted to keep `compute()` readable
- Lines 103-107:
  - standardizes what counts as completed
- Lines 109-110:
  - rounds final numeric values to 2 decimals

### `InventoryService.java`

- Lines 1-8:
  - package, imports, list utilities
- Line 10:
  - service class declaration
- Line 12:
  - unique validation code for inventory errors
- Lines 14-15:
  - dependencies
- Lines 17-20:
  - constructor injection
- Line 22:
  - cleaning entry point
- Line 23:
  - output list for valid records
- Line 25:
  - iterate over all fetched records
- Lines 26-49:
  - validation chain
  - each invalid condition logs an issue and skips the row
- Line 51:
  - keep only valid row
- Line 54:
  - return cleaned list
- Lines 57-59:
  - helper for blank-string validation

### `SalesService.java`

- structure is the same as `InventoryService`
- main difference is the business rules checked
- most important custom rule:
  - lines 50-52
  - quantity cannot be zero if revenue is positive

### `KpiCleaningSelfTest.java`

- Lines 1-27:
  - imports for models, services, repositories, and dates
- Line 29:
  - class declaration
- Line 31:
  - `main` method starts the self-test
- Line 32:
  - create shared exception source
- Lines 34-40:
  - construct each cleaning service with a stub repository
- Lines 42-48:
  - assert that invalid rows were removed
- Lines 51-53:
  - create one `AnalyticsInput` from all cleaned lists
- Line 54:
  - compute KPIs
- Lines 56-69:
  - verify every KPI result
- Line 71:
  - success message
- Lines 74-84:
  - lightweight assertion helpers
- Lines 86-191:
  - stub repositories return fixed data with both valid and invalid rows
  - these stubs let you test business logic without a real database

## 4. Design Patterns Used In Your Part

### Repository Pattern

Where:

- all cleaning services depend on repository interfaces

Why used:

- data access is kept separate from cleaning logic
- your services do not know SQL or database details

### Facade Pattern

Where:

- your part plugs into `DashboardService`, which acts as a simple entry point for the larger subsystem

Why used:

- the rest of the system does not need to know every cleaning step or KPI formula individually

### DTO / Data Carrier Style

Where:

- `AnalyticsInput`
- `KPIResultInternal`

Why used:

- one object carries all cleaned inputs into the engine
- one object carries all computed KPIs out of the engine

### Template-Like Validation Structure

Where:

- every cleaning service follows the same shape:
  - fetch raw data
  - validate row by row
  - report invalid rows
  - keep valid rows

Why used:

- makes the code predictable
- easier to add new service classes later

## 5. SOLID Principles In Your Part

### S - Single Responsibility Principle

Strongly followed.

Examples:

- services only clean data
- `AnalyticsEngine` only computes KPIs
- self-test only tests the logic

### O - Open/Closed Principle

Partly followed.

Examples:

- you can add new validation rules or new KPIs without rewriting the whole subsystem

Limitation:

- some service classes still need code edits if a new field is added

### L - Liskov Substitution Principle

Followed.

Examples:

- services depend on repository interfaces
- stubs in `KpiCleaningSelfTest` replace real repositories without breaking the service

This is one of your strongest viva points.

### I - Interface Segregation Principle

Followed.

Examples:

- each service depends only on its own repository interface
- no class is forced to depend on unrelated methods

### D - Dependency Inversion Principle

Followed in your part.

Examples:

- services depend on `InventoryRepositoryInterface`, `SalesRepositoryInterface`, and so on
- they do not depend directly on concrete repository implementations

## 6. GRASP Principles In Your Part

### Information Expert

Used well.

Examples:

- each service knows how to validate its own dataset
- `AnalyticsEngine` knows how to calculate KPIs because it owns the KPI rules

### Creator

Used in testing and orchestration.

Examples:

- `KpiCleaningSelfTest` creates the services and stub repositories it needs
- `AnalyticsEngine` creates the final KPI result object

### Controller

Visible at the subsystem level, but less central in your slice.

For your part:

- the cleaning services act as controlled processing steps inside the backend flow

### Low Coupling

Used well.

Examples:

- services are decoupled from database implementation
- engine is decoupled from repository logic

### High Cohesion

Used well.

Examples:

- each class has one clear purpose
- each service validates one specific data type

### Pure Fabrication

Used well.

Examples:

- service classes are not business entities themselves
- they exist to keep validation logic out of models and repositories

## 7. Good Viva Answers

### If asked: "What exactly was your contribution?"

You can say:

"My main contribution was the data cleaning layer and KPI engine. I worked on service classes that validate raw inventory, sales, orders, shipments, suppliers, warehouses, and forecasts before analytics runs. Then I implemented the KPI engine that computes totals, ratios, completion rates, forecast accuracy, and inventory coverage from the cleaned data."

### If asked: "Why did you clean data before computing KPIs?"

You can say:

"Because analytics is only meaningful if the input is valid. If we allow blank IDs, negative quantities, impossible dates, or inconsistent sales values, the KPIs become misleading. So the cleaning layer protects the engine from bad input."

### If asked: "Why did you use repository interfaces?"

You can say:

"That follows dependency inversion and low coupling. The service depends on an abstraction, not a concrete database class. It also made testing easier, because in the self-test I could swap the real repository with a stub repository."

### If asked: "What is the most important design pattern in your part?"

You can say:

"In my part, the repository pattern is the clearest one because the cleaning services read through repository interfaces instead of using direct database logic."

### If asked: "What is one limitation in your part?"

You can say:

"The warehouse utilization helper is still a placeholder, so the warehouse cleaning structure is correct, but richer source data is needed to compute utilization more accurately."
