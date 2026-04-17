import dashboard.DashboardService;
import dto.DashboardDTO;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class DBFile {

    public static void main(String[] args) {
        try {
            URL resource = DBFile.class.getClassLoader().getResource("database.properties");
            System.out.println("database.properties resource: " + resource);

            if (resource != null) {
                Properties properties = new Properties();
                try (InputStream inputStream = resource.openStream()) {
                    properties.load(inputStream);
                }
                System.out.println("db.url=" + properties.getProperty("db.url"));
                System.out.println("db.username=" + properties.getProperty("db.username"));
                String password = properties.getProperty("db.password");
                System.out.println("db.password.length=" + (password == null ? 0 : password.length()));
            }

            DashboardDTO dashboard = new DashboardService().buildDashboard();

            System.out.println("Analytics dashboard smoke test passed.");
            System.out.println("Insights: " + dashboard.getInsights().size());
            System.out.println("Alerts: " + dashboard.getAlerts().size());
            System.out.println("Revenue series size: " + dashboard.getVisualizations().getRevenueByProduct().size());
            System.out.println("Inventory series size: " + dashboard.getVisualizations().getInventoryLevels().size());
            System.out.println("Total revenue KPI: " + dashboard.getKpis().getTotalRevenue());
        } catch (Exception ex) {
            System.err.println("Analytics dashboard smoke test failed.");
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
