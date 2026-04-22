package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jackfruit.scm.database.adapter.ReportingAdapter;
import com.jackfruit.scm.database.facade.SupplyChainDatabaseFacade;
import com.jackfruit.scm.database.model.ReportingModels;
import dto.DashboardDTO;
import dto.KPIResult;
import dto.ReportDTO;
import dto.VisualizationDTO;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ReportExportService {
    private static final ObjectMapper JSON = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public record ExportedReport(String content, String mediaType, String fileName) {
    }

    public ExportedReport export(DashboardDTO dashboard, String requestedFormat, String sectionsCsv) {
        String format = normalizeFormat(requestedFormat);
        Set<String> sections = normalizeSections(sectionsCsv);
        Map<String, Object> payload = buildPayload(dashboard, sections);

        String content = switch (format) {
            case "csv" -> toCsv(payload);
            case "json" -> toJson(payload);
            default -> toText(payload);
        };

        String mediaType = switch (format) {
            case "csv" -> "text/csv";
            case "json" -> "application/json";
            default -> "text/plain";
        };

        return new ExportedReport(content, mediaType, "analytics-dashboard-report." + format);
    }

    private String normalizeFormat(String requestedFormat) {
        String format = requestedFormat == null ? "txt" : requestedFormat.trim().toLowerCase(Locale.ROOT);
        if (!format.equals("json") && !format.equals("csv") && !format.equals("txt")) {
            return "txt";
        }
        return format;
    }

    private Set<String> normalizeSections(String sectionsCsv) {
        if (sectionsCsv == null || sectionsCsv.isBlank()) {
            return Set.of("kpis", "insights", "alerts", "visualizations", "report", "margin", "reporting");
        }

        Set<String> sections = new TreeSet<>();
        for (String section : sectionsCsv.split(",")) {
            if (!section.isBlank()) {
                sections.add(section.trim().toLowerCase(Locale.ROOT));
            }
        }
        return sections;
    }

    private Map<String, Object> buildPayload(DashboardDTO dashboard, Set<String> sections) {
        LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
        payload.put("generatedAt", java.time.LocalDateTime.now().format(DATE_TIME));

        if (dashboard == null) {
            payload.put("error", "No dashboard data available.");
            return payload;
        }

        if (sections.contains("kpis")) {
            payload.put("kpis", extractKpis(dashboard.getKpis()));
        }
        if (sections.contains("insights")) {
            payload.put("insights", dashboard.getInsights());
        }
        if (sections.contains("alerts")) {
            payload.put("alerts", dashboard.getAlerts());
        }
        if (sections.contains("visualizations")) {
            payload.put("visualizations", extractVisualizations(dashboard.getVisualizations()));
        }
        if (sections.contains("report")) {
            payload.put("report", extractReport(dashboard.getReport()));
        }
        if (sections.contains("margin")) {
            payload.put("margin", extractMargin(dashboard));
        }
        if (sections.contains("reporting")) {
            payload.put("reportingAdapter", fetchReportingAdapterSummary());
        }

        return payload;
    }

    private Map<String, Object> extractKpis(KPIResult kpis) {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        if (kpis == null) {
            return values;
        }

        values.put("totalRevenue", kpis.getTotalRevenue());
        values.put("totalOrders", kpis.getTotalOrders());
        values.put("pendingOrders", kpis.getPendingOrders());
        values.put("completedOrders", kpis.getCompletedOrders());
        values.put("totalInventoryUnits", kpis.getTotalInventoryUnits());
        values.put("delayedShipments", kpis.getDelayedShipments());
        values.put("avgWarehouseUtilization", kpis.getAvgWarehouseUtilization());
        values.put("avgSupplierReliability", kpis.getAvgSupplierReliability());
        values.put("revenuePerOrder", kpis.getRevenuePerOrder());
        values.put("inventoryTurnoverRatio", kpis.getInventoryTurnoverRatio());
        values.put("forecastAccuracyRate", kpis.getForecastAccuracyRate());
        values.put("onTimeShipmentRate", kpis.getOnTimeShipmentRate());
        values.put("orderCompletionRate", kpis.getOrderCompletionRate());
        values.put("inventoryCoverageDays", kpis.getInventoryCoverageDays());
        return values;
    }

    private Map<String, Object> extractVisualizations(VisualizationDTO visualizations) {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        if (visualizations == null) {
            return values;
        }

        values.put("revenueByProduct", visualizations.getRevenueByProduct());
        values.put("inventoryLevels", visualizations.getInventoryLevels());
        return values;
    }

    private Map<String, Object> extractReport(ReportDTO report) {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        if (report == null) {
            return values;
        }

        values.put("summary", report.getSummary());
        values.put("generatedAt", report.getGeneratedAt() == null ? null : report.getGeneratedAt().format(DATE_TIME));
        values.put("insights", report.getInsights());
        values.put("alerts", report.getAlerts());
        return values;
    }

    private Map<String, Object> extractMargin(DashboardDTO dashboard) {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        if (dashboard.getMarginResult() == null) {
            return values;
        }

        values.put("marginConceded", dashboard.getMarginResult().marginConceded());
        values.put("marginProtected", dashboard.getMarginResult().marginProtected());
        if (dashboard.getMarginResult().period() != null) {
            values.put("periodStart", dashboard.getMarginResult().period().start());
            values.put("periodEnd", dashboard.getMarginResult().period().end());
        }
        return values;
    }

    private Map<String, Object> fetchReportingAdapterSummary() {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        try (SupplyChainDatabaseFacade facade = new SupplyChainDatabaseFacade()) {
            ReportingAdapter reportingAdapter = new ReportingAdapter(facade);
            values.put("dashboardRows", reportingAdapter.getDashboardReport().size());
            values.put("inventoryStockRows", reportingAdapter.getInventoryStockReport().size());
            values.put("priceDiscountRows", reportingAdapter.getPriceDiscountReport().size());
            values.put("exceptionRows", reportingAdapter.getExceptionReport().size());
        } catch (Exception ex) {
            values.put("error", ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return values;
    }

    private String toJson(Map<String, Object> payload) {
        try {
            return JSON.writeValueAsString(payload);
        } catch (Exception ex) {
            return "{\"error\":\"Unable to serialize report: " + escapeJson(ex.getMessage()) + "\"}";
        }
    }

    private String toText(Map<String, Object> payload) {
        StringBuilder builder = new StringBuilder();
        builder.append("ANALYTICS DASHBOARD REPORT\n");
        builder.append("Generated at: ").append(payload.get("generatedAt")).append("\n\n");

        payload.forEach((section, value) -> {
            if ("generatedAt".equals(section)) {
                return;
            }
            builder.append(section.toUpperCase(Locale.ROOT)).append('\n');
            appendTextValue(builder, value, 2);
            builder.append('\n');
        });

        return builder.toString();
    }

    private void appendTextValue(StringBuilder builder, Object value, int indent) {
        String padding = " ".repeat(indent);
        if (value instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                builder.append(padding).append(entry.getKey()).append(": ");
                appendInlineValue(builder, entry.getValue());
                builder.append('\n');
            }
            return;
        }

        if (value instanceof List<?> list) {
            for (int i = 0; i < list.size(); i++) {
                builder.append(padding).append("- ");
                appendInlineValue(builder, list.get(i));
                builder.append('\n');
            }
            if (list.isEmpty()) {
                builder.append(padding).append("(none)\n");
            }
            return;
        }

        builder.append(padding);
        appendInlineValue(builder, value);
        builder.append('\n');
    }

    private void appendInlineValue(StringBuilder builder, Object value) {
        if (value instanceof Map<?, ?> map) {
            builder.append(map.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining(", ")));
            return;
        }

        if (value instanceof List<?> list) {
            builder.append(String.join(", ", list.stream().map(String::valueOf).toList()));
            return;
        }

        builder.append(String.valueOf(value));
    }

    private String toCsv(Map<String, Object> payload) {
        StringBuilder builder = new StringBuilder();
        builder.append("Section,Key,Value\n");

        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            if ("generatedAt".equals(entry.getKey())) {
                builder.append("metadata,generatedAt,\"").append(escapeCsv(String.valueOf(entry.getValue()))).append("\"\n");
                continue;
            }

            appendCsvSection(builder, entry.getKey(), entry.getValue());
        }

        return builder.toString();
    }

    private void appendCsvSection(StringBuilder builder, String section, Object value) {
        if (value instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                builder.append(section).append(',')
                        .append('"').append(escapeCsv(String.valueOf(entry.getKey()))).append('"')
                        .append(',')
                        .append('"').append(escapeCsv(String.valueOf(entry.getValue()))).append('"')
                        .append('\n');
            }
            return;
        }

        if (value instanceof List<?> list) {
            for (int i = 0; i < list.size(); i++) {
                builder.append(section).append(',')
                        .append('"').append("item_").append(i + 1).append('"')
                        .append(',')
                        .append('"').append(escapeCsv(String.valueOf(list.get(i)))).append('"')
                        .append('\n');
            }
            if (list.isEmpty()) {
                builder.append(section).append(",item_1,\"\"\n");
            }
            return;
        }

        builder.append(section).append(",value,\"")
                .append(escapeCsv(String.valueOf(value))).append("\"\n");
    }

    private String escapeCsv(String value) {
        return value == null ? "" : value.replace("\"", "\"\"");
    }

    private String escapeJson(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
