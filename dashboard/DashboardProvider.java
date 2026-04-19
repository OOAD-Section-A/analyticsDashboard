package dashboard;

import dto.DashboardDTO;

public interface DashboardProvider {
    DashboardDTO buildDashboard();
}