import collector.DataCollector;
import integrator.*;
import db.DBConnection;

import java.sql.Connection;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        try {
            // Step 1: Get DB Connection
            Connection conn = DBConnection.getConnection();

            // Step 2: Data Collection
            DataCollector collector = new DataCollector(conn);
            Map<String, Object> rawData = collector.collectData();

            System.out.println("Raw Data Collected: " + rawData.keySet());

            // Step 3: Data Integration
            DataIntegrator integrator = new DataIntegrator();
            UnifiedData unifiedData = integrator.integrate(rawData);

            System.out.println("\nUnified Data Created:");
            System.out.println("Inventory count: " + unifiedData.inventory.size());
            System.out.println("Orders count: " + unifiedData.orders.size());

            // Close DB connection
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}