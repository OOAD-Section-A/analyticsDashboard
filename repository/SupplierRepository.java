package repository;

import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.config.DatabaseConnectionManager;
import com.jackfruit.scm.database.facade.subsystem.ReportingSubsystemFacade;
import com.jackfruit.scm.database.model.ReportingModels;
import exception.AnalyticsExceptionSource;
import model.SupplierData;
import repository.interfaces.SupplierRepositoryInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SupplierRepository implements SupplierRepositoryInterface {

    private static final int CONNECTION_FAILURE_ID = 1006;

    private final AnalyticsExceptionSource exceptionSource;

    public SupplierRepository(AnalyticsExceptionSource exceptionSource) {
        this.exceptionSource = exceptionSource;
    }

    public List<SupplierData> fetchAll() {
        List<SupplierData> suppliersFromMaster = fetchFromMasterTable();
        if (!suppliersFromMaster.isEmpty()) {
            return suppliersFromMaster;
        }

        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            ReportingSubsystemFacade reporting = facade.reporting();
            LinkedHashMap<String, SupplierData> suppliers = new LinkedHashMap<>();

            for (ReportingModels.DashboardReportRow row : reporting.getDashboardReport()) {
                if (row.supplierId() == null || row.supplierId().isBlank()) {
                    continue;
                }

                suppliers.putIfAbsent(
                        row.supplierId(),
                        new SupplierData(
                                row.supplierId(),
                                row.supplierName(),
                                "",
                                row.supplierPerformanceScore() == null ? 0.0 : row.supplierPerformanceScore()
                        )
                );
            }

            return new ArrayList<>(suppliers.values());
        } catch (Exception ex) {
            throw RepositoryExceptionSupport.fail(exceptionSource, "SupplierRepository.fetchAll", CONNECTION_FAILURE_ID, ex);
        }
    }

    private List<SupplierData> fetchFromMasterTable() {
        String sql = """
                SELECT supplier_id, name, status, reliability_score
                FROM proc_suppliers
                ORDER BY supplier_id
                """;

        LinkedHashMap<String, SupplierData> suppliers = new LinkedHashMap<>();

        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String supplierId = resultSet.getString("supplier_id");
                if (supplierId == null || supplierId.isBlank()) {
                    continue;
                }

                suppliers.putIfAbsent(
                        supplierId,
                        new SupplierData(
                                supplierId,
                                resultSet.getString("name"),
                                resultSet.getString("status"),
                                resultSet.getDouble("reliability_score")
                        )
                );
            }

            return new ArrayList<>(suppliers.values());
        } catch (Exception ex) {
            return List.of();
        }
    }
}
