package com.pricingos.reporting;

import com.pricingos.common.IMarginProfitabilityService;
import com.pricingos.common.MarginProfitabilityResult;
import com.pricingos.common.MarginProfitabilityResult.DateRange;
import com.jackfruit.scm.database.config.DatabaseConnectionManager; // Existing manager

import java.sql.*;
import java.time.LocalDateTime;

public class MarginProfitabilityServiceImpl implements IMarginProfitabilityService {

    @Override
    public MarginProfitabilityResult getMarginProfitabilitySummary(LocalDateTime start, LocalDateTime end) {
        double marginConceded = fetchSum("APPROVED", start, end);
        double marginProtected = fetchSum("REJECTED", start, end);

        return new MarginProfitabilityResult(marginConceded, marginProtected, new DateRange(start, end));
    }

    private double fetchSum(String status, LocalDateTime start, LocalDateTime end) {
        // Query provided in the Integration Guide
        String sql = "SELECT COALESCE(SUM(discount_amount), 0) FROM profitability_analytics " +
                     "WHERE final_status = ? AND recorded_at >= ? AND recorded_at <= ?";
        
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setObject(2, Timestamp.valueOf(start));
            stmt.setObject(3, Timestamp.valueOf(end));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}