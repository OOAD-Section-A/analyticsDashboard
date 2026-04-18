# Reporting & Analytics Dashboard Integration Guide

## Problem Faced
- Unable to connect to the database using the provided JAR and adapter classes.
- Encountered authentication errors ("Access denied for user 'root'@'localhost'") and empty query results.
- Issues with `database.properties` not being picked up or having incorrect credentials.

## Solution That Worked
- Placed the latest `database-module-1.0.0-SNAPSHOT-standalone.jar` in the `lib/` folder.
- Ensured `database.properties` (with correct DB username and password) is in the project root and classpath.
- Started the database server locally with matching credentials.
- Inserted sample data into the relevant tables for testing.
- Used the adapter classes (e.g., `InventoryAdapter`) in Java code to fetch and print data successfully.

## Key Commands
- Compile: `javac -cp "lib/database-module-1.0.0-SNAPSHOT-standalone.jar" DBFile.java`
- Run: `java -cp ".;lib/database-module-1.0.0-SNAPSHOT-standalone.jar" DBFile`

---
If you face connection issues, double-check your `database.properties` and ensure the DB server is running with the correct credentials.