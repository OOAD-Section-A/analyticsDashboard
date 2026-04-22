# Analytics Dashboard

Reports and analytics dashboard for the supply chain management project.

## What Is Included

- Spring Boot backend in `com.analytics`, `dashboard`, `service`, `engine`, `repository`, `dto`, `model`, `internal`, and `common`
- Java Swing desktop UI in `desktop/SwingDashboardApp.java`
- Database integration through the shared database module JARs in `lib/`
- Report export endpoint with TXT, CSV, and JSON downloads
- Table data explorer with dataset and column filters
- Analytics visualizations for demand trend and supplier performance

## Prerequisites

- Java 17 or later
- MySQL running locally
- The database credentials in `database.properties` must point to your local MySQL instance

## Local Run Steps

### 1. Load demo data

If you want the dashboard to show real rows instead of empty results, run:

```bash
mysql -u root -p OOAD < analyticsDashboard/sql/seed-local-demo.sql
```

If `mysql` is not on your PATH, use the full path to `mysql.exe` or load the script in MySQL Workbench and execute it there.

### 2. Compile the project

From the project root:

```bash
mvn -q -DskipTests compile
```

### 3. Start the Swing desktop app

Run the desktop launcher after compilation:

```bash
java -cp "target/classes;lib/*" desktop.SwingDashboardApp
```

The Swing app opens as a desktop window and talks directly to the analytics services.

## Main API Endpoints

- `GET /api/dashboard`
- `GET /api/dashboard/report?format=txt|csv|json&sections=kpis,alerts,insights,visualizations,report,margin,reporting`
- `GET /api/dashboard/table-view?dataset=sales|inventory|orders|shipments|warehouses|suppliers|forecasts`

## Seed File

The file [`sql/seed-local-demo.sql`](sql/seed-local-demo.sql) inserts local demo rows into the current OOAD schema.

It is meant for your machine only:

- it updates your local MySQL `OOAD` database
- it does not change the shared database module JAR
- it can be rerun safely because most inserts use `ON DUPLICATE KEY UPDATE`

## Documentation

The detailed file-by-file guide is here:

- [`docs/FILE_GUIDE.md`](docs/FILE_GUIDE.md)

Additional integration notes are in `docs/`.

## Notes

- Keep `database.properties` configured with valid MySQL credentials.
- The Swing desktop app uses the same dashboard services, report export service, and table explorer service as the backend.
