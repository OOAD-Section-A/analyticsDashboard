# Analytics Dashboard Demo UI

A simple React application to demonstrate the Java-based analytics subsystem.

## Setup

1. Navigate to the `ui` folder:
   ```
   cd ui
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Start the development server:
   ```
   npm start
   ```

The app will run on `http://localhost:3000` and fetch data from `http://localhost:8080/api/dashboard`.

## Features

- Fetches dashboard data from the Java backend
- Displays KPI cards (Total Revenue, Total Orders, Inventory Value)
- Shows alerts and insights as lists
- Displays report summary
- Simple chart placeholder

## Notes

- This is a minimal demo UI for demonstration purposes only
- Ensure the Java backend is running on port 8080
- CORS may need to be configured on the backend for cross-origin requests