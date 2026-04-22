# Analytics Dashboard

Reports and analytics dashboard for the supply chain management project.

## What Is Included

- Spring Boot backend in `com.analytics`, `dashboard`, `service`, `engine`, `repository`, `dto`, `model`, `internal`, and `common`
- React frontend in `ui/src`
- Database integration through the shared database module JARs in `lib/`
- Report export endpoint with TXT, CSV, and JSON downloads
- Table data explorer with dataset and column filters
- Analytics visualizations for demand trend and supplier performance

## Prerequisites

- Java 17 or later
- Node.js and npm
- MySQL running locally
- The database credentials in `database.properties` must point to your local MySQL instance

## Local Run Steps

### 1. Load demo data

If you want the dashboard to show real rows instead of empty results, run:

```bash
mysql -u root -p OOAD < analyticsDashboard/sql/seed-local-demo.sql
```

If `mysql` is not on your PATH, use the full path to `mysql.exe` or load the script in MySQL Workbench and execute it there.

### 2. Start the backend

From the project root:

```bash
mvn spring-boot:run
```

The backend runs on `http://localhost:8080`.

### 3. Start the frontend

In a second terminal:

```bash
cd ui
npm install
npm start
```

The React app runs on `http://localhost:3000` and calls the backend on port `8080`.

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
- The frontend report download uses the backend export endpoint, so the file matches the selected on-screen sections.
