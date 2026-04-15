# Supply Chain Management (SCM) Dashboard

## Overview
This project is a modular Java-based Supply Chain Management (SCM) dashboard. It provides a framework for collecting, integrating, and unifying data from various SCM domains such as inventory, orders, suppliers, shipments, and sales. The code is organized for easy extension and integration with other systems.

## Project Structure

- `Main.java` — Entry point; orchestrates DB connection, data collection, and integration.
- `collector/`
  - `DataCollector.java` — Fetches raw data from the database using core modules.
- `core/`
  - `SCMCoreModules.java` — Contains methods to retrieve data for Inventory, Orders, Suppliers, Shipments, and Sales from the database.
- `db/`
  - `DBConnection.java` — Handles database connection (currently set up for MySQL).
- `integrator/`
  - `DataIntegrator.java` — Integrates and synchronizes raw data into a unified structure, ensuring consistency across modules.
  - `UnifiedData.java` — Data structure holding unified lists for all SCM domains.
- `model/`
  - `Inventory.java`, `Order.java`, `Supplier.java`, `Shipment.java`, `Sales.java` — Data models representing SCM entities.

## How It Works

1. **Database Connection**: `DBConnection.getConnection()` establishes a connection to a MySQL database (edit credentials in `db/DBConnection.java` as needed).
2. **Data Collection**: `DataCollector` uses `SCMCoreModules` to fetch all raw datasets from the database.
3. **Data Integration**: `DataIntegrator` synchronizes and unifies the raw data, filling in missing entries for consistency.
4. **Output**: The main method prints the keys of collected raw data and counts of unified data entities.

## Running the Project

### Prerequisites
- Java 8 or higher
- MySQL database running with the following tables: `inventory`, `orders`, `suppliers`, `shipments`, `sales`
- MySQL JDBC driver in your classpath

### Steps
1. **Configure Database**
   - Edit `db/DBConnection.java` to set your MySQL URL, username, and password.
2. **Compile**
   - From the `dashboard` directory, run:
     ```sh
     javac -cp .;path/to/mysql-connector-java.jar Main.java collector/*.java core/*.java db/*.java integrator/*.java model/*.java
     ```
     (On Unix, replace `;` with `:`)
3. **Run**
   - Run the main class:
     ```sh
     java -cp .;path/to/mysql-connector-java.jar Main
     ```

## Integration Points
- All data models are in `model/` and can be extended with new fields as needed.
- To add new data sources or modules, extend `SCMCoreModules` and update `DataCollector`.
- The integration logic in `DataIntegrator` can be customized for additional consistency checks or business rules.

## Work Done So Far
- Modularized SCM data access and integration.
- Implemented robust data models for inventory, orders, suppliers, shipments, and sales.
- Provided a unified data structure for downstream analytics or dashboarding.
- Ensured easy extensibility for new SCM modules or integration with other systems.

## Next Steps for Integration
- Add your own modules by following the structure in `core/` and `model/`.
- Integrate with external APIs or data sources by extending the data collection and integration logic.
- Build a UI or reporting layer on top of the unified data.

---