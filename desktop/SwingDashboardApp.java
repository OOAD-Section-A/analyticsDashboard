package desktop;

import dashboard.DashboardService;
import dto.DashboardDTO;
import dto.KPIResult;
import dto.TableViewDTO;
import dto.VisualizationDTO;
import service.ReportExportService;
import service.TableViewService;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SwingDashboardApp {
    private final DashboardService dashboardService = new DashboardService();
    private final ReportExportService reportExportService = new ReportExportService();
    private final TableViewService tableViewService = new TableViewService();

    private final JFrame frame = new JFrame("Report And Analytics Dashboard");
    private final JLabel statusLabel = new JLabel("Loading dashboard...");
    private final JComboBox<String> reportFormatCombo = new JComboBox<>(new String[]{"txt", "csv", "json"});
    private final JComboBox<String> datasetCombo = new JComboBox<>();
    private final Map<String, JCheckBox> sectionToggles = new LinkedHashMap<>();
    private final Map<String, JLabel> kpiValueLabels = new LinkedHashMap<>();
    private final JPanel analyticsSection = new JPanel(new GridLayout(1, 2, 16, 16));
    private final JPanel insightsListPanel = new JPanel();
    private final JPanel alertsListPanel = new JPanel();
    private final JPanel tableColumnsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JTable table = new JTable();
    private final JTextArea reportArea = new JTextArea(6, 60);
    private final JTextArea marginArea = new JTextArea(4, 60);
    private final LineChartPanel demandChart = new LineChartPanel();
    private final BarChartPanel performanceChart = new BarChartPanel();
    private final JPanel kpiGrid = new JPanel(new GridLayout(2, 4, 12, 12));
    private final JPanel explorerTableWrap = new JPanel(new BorderLayout());

    private DashboardDTO dashboardData;
    private TableViewDTO tableData;
    private final List<String> selectedColumns = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingDashboardApp().show());
    }

    private void show() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1320, 900));
        frame.setSize(1500, 980);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        frame.add(buildHeader(), BorderLayout.NORTH);
        frame.add(buildContent(), BorderLayout.CENTER);
        frame.add(buildStatusBar(), BorderLayout.SOUTH);

        reportArea.setEditable(false);
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);
        reportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        marginArea.setEditable(false);
        marginArea.setLineWrap(true);
        marginArea.setWrapStyleWord(true);
        marginArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        loadDatasets();
        datasetCombo.addActionListener(e -> reloadTableData());
        refreshAll();
        frame.setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 245, 245));
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 16, 24));

        JPanel titleWrap = new JPanel();
        titleWrap.setOpaque(false);
        titleWrap.setLayout(new BoxLayout(titleWrap, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Report And Analytics Dashboard");
        title.setForeground(new Color(31, 41, 55));
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitle = new JLabel("Supply chain reporting, analytics, and export controls");
        subtitle.setForeground(new Color(75, 85, 99));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        titleWrap.add(title);
        titleWrap.add(Box.createVerticalStrut(8));
        titleWrap.add(subtitle);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);

        JLabel formatLabel = new JLabel("Format");
        formatLabel.setForeground(new Color(31, 41, 55));
        reportFormatCombo.setPreferredSize(new Dimension(92, 30));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> refreshAll());

        JButton exportButton = new JButton("Download report");
        exportButton.setFocusPainted(false);
        exportButton.addActionListener(e -> downloadReport());

        controls.add(formatLabel);
        controls.add(reportFormatCombo);
        controls.add(refreshButton);
        controls.add(exportButton);

        header.add(titleWrap, BorderLayout.WEST);
        header.add(controls, BorderLayout.EAST);
        return header;
    }

    private JScrollPane buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder());

        content.add(buildFilterPanel());
        content.add(buildSectionPanel("Key Performance Indicators", kpiGrid));
        content.add(buildAnalyticsPanel());
        content.add(buildSectionPanel("Insights", insightsListPanel));
        content.add(buildSectionPanel("Alerts", alertsListPanel));
        content.add(buildExplorerPanel());
        content.add(buildSectionPanel("Report Summary", buildReportPanel()));
        content.add(buildSectionPanel("Margin Profitability", buildMarginPanel()));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        return scrollPane;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        bar.add(statusLabel, BorderLayout.WEST);
        return bar;
    }

    private JPanel buildFilterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

    // ===== Heading =====
        JLabel heading = new JLabel("Filter");
        heading.setFont(new Font("SansSerif", Font.BOLD, 21));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        heading.setMaximumSize(new Dimension(Integer.MAX_VALUE, heading.getPreferredSize().height));

    // ===== Helper text =====
        JLabel helper = new JLabel("Choose which sections to view and which dataset to inspect.");
        helper.setForeground(new Color(75, 85, 99));
        helper.setAlignmentX(Component.LEFT_ALIGNMENT);
        helper.setMaximumSize(new Dimension(Integer.MAX_VALUE, helper.getPreferredSize().height));

    // ===== Toggles =====
        JPanel toggles = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toggles.setOpaque(false);
        toggles.setAlignmentX(Component.LEFT_ALIGNMENT);

        addToggle(toggles, "kpis", "KPI cards", true);
        addToggle(toggles, "analytics", "Analytics", true);
        addToggle(toggles, "insights", "Insights", true);
        addToggle(toggles, "alerts", "Alerts", true);
        addToggle(toggles, "explorer", "Data explorer", true);
        addToggle(toggles, "report", "Report", true);
        addToggle(toggles, "margin", "Profitability", true);

        sectionToggles.forEach((key, checkbox) ->
                checkbox.addActionListener(e -> applySectionVisibility())
        );

    // ===== Bottom note =====
        JLabel note = new JLabel("Use the explorer to switch datasets and columns.");
        note.setForeground(new Color(75, 85, 99));
        note.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel tableNote = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        tableNote.setOpaque(false);
        tableNote.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableNote.add(note);

    // ===== Add everything directly (NO BorderLayout wrapper) =====
        panel.add(heading);
        panel.add(Box.createVerticalStrut(4));
        panel.add(helper);
        panel.add(Box.createVerticalStrut(10));
        panel.add(toggles);
        panel.add(Box.createVerticalStrut(6));
        panel.add(tableNote);

        return panel;
    }

    private void addToggle(JPanel panel, String key, String label, boolean selected) {
        JCheckBox checkbox = new JCheckBox(label, selected);
        checkbox.setOpaque(false);
        checkbox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sectionToggles.put(key, checkbox);
        panel.add(checkbox);
    }

    private JPanel buildSectionPanel(String title, JPanel inner) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        section.setBackground(Color.WHITE);
        section.setName(title);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 21));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        inner.setAlignmentX(Component.LEFT_ALIGNMENT);
        inner.setOpaque(false);
        inner.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        section.add(heading);
        section.add(Box.createVerticalStrut(10));
        section.add(inner);
        return section;
    }

    private JPanel buildAnalyticsPanel() {
        analyticsSection.setOpaque(false);
        analyticsSection.removeAll();
        analyticsSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        analyticsSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        analyticsSection.add(buildAnalyticsCard("Demand Trend", demandChart,
                "Revenue by product from the current dashboard data."));
        analyticsSection.add(buildAnalyticsCard("Supplier Performance", performanceChart,
                "Bar view derived from KPI performance signals."));
        return analyticsSection;
    }

    private JPanel buildAnalyticsCard(String title, JPanel chartPanel, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel(description);
        desc.setForeground(new Color(75, 85, 99));
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(heading);
        card.add(Box.createVerticalStrut(4));
        card.add(desc);
        card.add(Box.createVerticalStrut(8));
        chartPanel.setPreferredSize(new Dimension(500, 220));
        chartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(chartPanel);
        return card;
    }

    private JPanel buildExplorerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel title = new JLabel("Data Explorer");
        title.setFont(new Font("SansSerif", Font.BOLD, 21));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel helper = new JLabel("Pick a dataset, then choose which columns you want to show.");
        helper.setForeground(new Color(75, 85, 99));
        helper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(title, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(new JLabel("Dataset"));
        datasetCombo.setPreferredSize(new Dimension(180, 30));
        right.add(datasetCombo);
        header.add(right, BorderLayout.EAST);

        panel.add(header);
        panel.add(Box.createVerticalStrut(6));
        panel.add(helper);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tableColumnsPanel);
        panel.add(Box.createVerticalStrut(8));
        explorerTableWrap.setOpaque(false);
        explorerTableWrap.setBorder(BorderFactory.createEmptyBorder());
        explorerTableWrap.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(explorerTableWrap);
        return panel;
    }

    private JPanel buildReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        reportArea.setBackground(Color.WHITE);
        reportArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMarginPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        marginArea.setBackground(Color.WHITE);
        marginArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(new JScrollPane(marginArea), BorderLayout.CENTER);
        return panel;
    }

    private void loadDatasets() {
        datasetCombo.removeAllItems();
        for (String dataset : tableViewService.supportedDatasets()) {
            datasetCombo.addItem(dataset);
        }
        datasetCombo.setSelectedItem("sales");
    }

    private void refreshAll() {
        statusLabel.setText("Loading dashboard data...");
        new SwingWorker<DashboardDTO, Void>() {
            @Override
            protected DashboardDTO doInBackground() {
                return dashboardService.buildDashboard();
            }

            @Override
            protected void done() {
                try {
                    dashboardData = get();
                    updateDashboardView();
                    statusLabel.setText("Dashboard refreshed at " + LocalDateTime.now());
                } catch (Exception ex) {
                    showError("Failed to load dashboard", ex);
                }
            }
        }.execute();

        reloadTableData();
    }

    private void updateDashboardView() {
        if (dashboardData == null) {
            return;
        }

        populateKpis(dashboardData.getKpis());
        populateInsights(dashboardData.getInsights());
        populateAlerts(dashboardData.getAlerts());
        populateAnalytics(dashboardData.getVisualizations());
        populateReport(dashboardData);
        populateMargin(dashboardData);
        applySectionVisibility();
    }

    private void populateKpis(KPIResult kpis) {
        kpiGrid.removeAll();
        kpiValueLabels.clear();

        addKpiCard("Total Revenue", formatCurrency(kpis == null ? null : kpis.getTotalRevenue()));
        addKpiCard("Total Orders", formatNumber(kpis == null ? null : kpis.getTotalOrders()));
        addKpiCard("Inventory Units", formatNumber(kpis == null ? null : kpis.getTotalInventoryUnits()));
        addKpiCard("On-Time Shipments", formatPercent(kpis == null ? null : kpis.getOnTimeShipmentRate()));
        addKpiCard("Warehouse Utilization", formatPercent(kpis == null ? null : kpis.getAvgWarehouseUtilization()));
        addKpiCard("Forecast Accuracy", formatPercent(kpis == null ? null : kpis.getForecastAccuracyRate()));
        addKpiCard("Pending Orders", formatNumber(kpis == null ? null : kpis.getPendingOrders()));
        addKpiCard("Delayed Shipments", formatNumber(kpis == null ? null : kpis.getDelayedShipments()));
        kpiGrid.revalidate();
        kpiGrid.repaint();
    }

    private void addKpiCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setPreferredSize(new Dimension(220, 120));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(55, 65, 81));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(new Color(17, 24, 39));
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 26));

        card.add(titleLabel);
        card.add(Box.createVerticalGlue());
        card.add(valueLabel);
        kpiValueLabels.put(title, valueLabel);
        kpiGrid.add(card);
    }

    private void populateInsights(List<String> insights) {
        populateBulletList(insightsListPanel, insights, Color.WHITE, new Color(226, 232, 240));
    }

    private void populateAlerts(List<String> alerts) {
        populateBulletList(alertsListPanel, alerts, Color.WHITE, new Color(226, 232, 240));
    }

    private void populateBulletList(JPanel container, List<String> values, Color bg, Color accent) {
        container.removeAll();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        if (values == null || values.isEmpty()) {
            JLabel empty = new JLabel("No data available");
            empty.setForeground(new Color(100, 116, 139));
            container.add(empty);
        } else {
            for (String value : values) {
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(Color.WHITE);
                row.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(226, 232, 240)),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
                JLabel label = new JLabel(value);
                label.setForeground(new Color(31, 41, 55));
                row.add(label, BorderLayout.CENTER);
                container.add(row);
                container.add(Box.createVerticalStrut(6));
            }
        }

        container.revalidate();
        container.repaint();
    }

    private void populateAnalytics(VisualizationDTO visualizations) {
        Map<String, Double> revenueByProduct = visualizations == null ? Map.of() : visualizations.getRevenueByProduct();
        Map<String, Integer> inventoryLevels = visualizations == null ? Map.of() : visualizations.getInventoryLevels();

        demandChart.setData(revenueByProduct);
        performanceChart.setData(inventoryLevels);
        demandChart.repaint();
        performanceChart.repaint();
    }

    private void populateReport(DashboardDTO dashboard) {
        if (dashboard.getReport() == null) {
            reportArea.setText("No report summary available.");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(dashboard.getReport().getSummary()).append("\n\n");
        builder.append("Generated at: ").append(dashboard.getReport().getGeneratedAt()).append("\n\n");
        builder.append("Insights:\n");
        appendLines(builder, dashboard.getReport().getInsights());
        builder.append("\nAlerts:\n");
        appendLines(builder, dashboard.getReport().getAlerts());
        reportArea.setText(builder.toString());
        reportArea.setCaretPosition(0);
    }

    private void populateMargin(DashboardDTO dashboard) {
        if (dashboard.getMarginResult() == null) {
            marginArea.setText("No margin data available.");
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Margin Conceded: ").append(formatCurrency(dashboard.getMarginResult().marginConceded())).append("\n");
        builder.append("Margin Protected: ").append(formatCurrency(dashboard.getMarginResult().marginProtected())).append("\n");
        if (dashboard.getMarginResult().period() != null) {
            builder.append("Period Start: ").append(dashboard.getMarginResult().period().start()).append("\n");
            builder.append("Period End: ").append(dashboard.getMarginResult().period().end()).append("\n");
        }
        marginArea.setText(builder.toString());
        marginArea.setCaretPosition(0);
    }

    private void appendLines(StringBuilder builder, List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            builder.append("(none)\n");
            return;
        }
        for (String line : lines) {
            builder.append("- ").append(line).append("\n");
        }
    }

    private void reloadTableData() {
        String dataset = Objects.toString(datasetCombo.getSelectedItem(), "sales");
        statusLabel.setText("Loading table data for " + dataset + "...");
        new SwingWorker<TableViewDTO, Void>() {
            @Override
            protected TableViewDTO doInBackground() {
                return tableViewService.buildTableView(dataset);
            }

            @Override
            protected void done() {
                try {
                    tableData = get();
                    populateTableColumns();
                    statusLabel.setText("Loaded dataset: " + dataset);
                } catch (Exception ex) {
                    showError("Failed to load table data", ex);
                }
            }
        }.execute();
    }

    private void populateTableColumns() {
        tableColumnsPanel.removeAll();
        selectedColumns.clear();

        if (tableData == null) {
            tableColumnsPanel.add(new JLabel("No table data loaded."));
            tableColumnsPanel.revalidate();
            tableColumnsPanel.repaint();
            return;
        }

        for (String column : tableData.getColumns()) {
            JCheckBox box = new JCheckBox(column, true);
            box.addActionListener(e -> rebuildTableModel());
            tableColumnsPanel.add(box);
            selectedColumns.add(column);
        }

        rebuildTableModel();
        tableColumnsPanel.revalidate();
        tableColumnsPanel.repaint();
    }

    private void rebuildTableModel() {
        if (tableData == null) {
            table.setModel(new DefaultTableModel());
            return;
        }

        List<String> activeColumns = new ArrayList<>();
        for (int i = 0; i < tableColumnsPanel.getComponentCount(); i++) {
            if (tableColumnsPanel.getComponent(i) instanceof JCheckBox checkBox && checkBox.isSelected()) {
                activeColumns.add(checkBox.getText());
            }
        }

        DefaultTableModel model = new DefaultTableModel();
        for (String column : activeColumns) {
            model.addColumn(column);
        }

        for (Map<String, Object> row : tableData.getRows()) {
            List<Object> values = new ArrayList<>();
            for (String column : activeColumns) {
                values.add(row.getOrDefault(column, ""));
            }
            model.addRow(values.toArray());
        }

        table.setModel(model);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < activeColumns.size(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(160);
        }
    }

    private void applySectionVisibility() {
        toggleSection("Key Performance Indicators", isSelected("kpis"));
        toggleSection("Analytics", isSelected("analytics"));
        toggleSection("Insights", isSelected("insights"));
        toggleSection("Alerts", isSelected("alerts"));
        toggleSection("Data Explorer", isSelected("explorer"));
        toggleSection("Report Summary", isSelected("report"));
        toggleSection("Margin Profitability", isSelected("margin"));
    }

    private boolean isSelected(String key) {
        JCheckBox checkbox = sectionToggles.get(key);
        return checkbox == null || checkbox.isSelected();
    }

    private void toggleSection(String title, boolean visible) {
        ContainerUtil.forEach(frame.getContentPane(), component -> {
            if (component instanceof JScrollPane scrollPane) {
                component = scrollPane.getViewport().getView();
            }
            if (component instanceof JPanel panel && title.equals(panel.getName())) {
                panel.setVisible(visible);
            }
        });
        frame.revalidate();
        frame.repaint();
    }

    private void downloadReport() {
        if (dashboardData == null) {
            JOptionPane.showMessageDialog(frame, "Dashboard data is not loaded yet.", "Report", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String format = Objects.toString(reportFormatCombo.getSelectedItem(), "txt");
        String sections = sectionToggles.entrySet().stream()
                .filter(entry -> entry.getValue().isSelected())
                .map(Map.Entry::getKey)
                .map(this::sectionNameToExportKey)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(","));

        ReportExportService.ExportedReport exported = reportExportService.export(dashboardData, format, sections);

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(exported.fileName()));
        int result = chooser.showSaveDialog(frame);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File target = chooser.getSelectedFile();
        try {
            Files.writeString(target.toPath(), exported.content(), StandardCharsets.UTF_8);
            JOptionPane.showMessageDialog(frame, "Report saved to " + target.getAbsolutePath(), "Report", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Unable to save report", ex);
        }
    }

    private String sectionNameToExportKey(String key) {
        return switch (key) {
            case "kpis" -> "kpis";
            case "analytics" -> "visualizations";
            case "insights" -> "insights";
            case "alerts" -> "alerts";
            case "report" -> "report";
            case "margin" -> "margin";
            case "explorer" -> "reporting";
            default -> null;
        };
    }

    private String formatCurrency(Double value) {
        if (value == null) {
            return "N/A";
        }
        return "$" + String.format("%,.2f", value);
    }

    private String formatNumber(Number value) {
        if (value == null) {
            return "N/A";
        }
        return String.format("%,d", value.longValue());
    }

    private String formatPercent(Double value) {
        if (value == null) {
            return "N/A";
        }
        return String.format("%.2f%%", value);
    }

    private void showError(String title, Exception ex) {
        statusLabel.setText(title + ": " + ex.getMessage());
        JOptionPane.showMessageDialog(frame, ex.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }

    private static final class LineChartPanel extends JPanel {
        private List<String> labels = List.of();
        private List<Double> values = List.of();

        private LineChartPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        }

        private void setData(Map<String, Double> data) {
            if (data == null || data.isEmpty()) {
                labels = List.of();
                values = List.of();
                return;
            }
            labels = new ArrayList<>(data.keySet());
            values = new ArrayList<>(data.values());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int left = 42;
            int top = 20;
            int right = 18;
            int bottom = 34;

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);

            if (values.isEmpty()) {
                g2.setColor(new Color(100, 116, 139));
                g2.drawString("No data available", width / 2 - 50, height / 2);
                g2.dispose();
                return;
            }

            int chartWidth = width - left - right;
            int chartHeight = height - top - bottom;
            double maxValue = Math.max(values.stream().mapToDouble(Double::doubleValue).max().orElse(1d), 1d);

            g2.setColor(new Color(203, 213, 225));
            g2.drawLine(left, height - bottom, width - right, height - bottom);
            g2.drawLine(left, top, left, height - bottom);

            Path2D path = new Path2D.Double();
            for (int i = 0; i < values.size(); i++) {
                double x = left + (chartWidth * i) / Math.max(values.size() - 1, 1);
                double y = top + chartHeight - (chartHeight * values.get(i)) / maxValue;
                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }

            g2.setStroke(new BasicStroke(3f));
            g2.setColor(new Color(55, 65, 81));
            g2.draw(path);

            g2.setColor(new Color(55, 65, 81));
            for (int i = 0; i < values.size(); i++) {
                double x = left + (chartWidth * i) / Math.max(values.size() - 1, 1);
                double y = top + chartHeight - (chartHeight * values.get(i)) / maxValue;
                g2.fillOval((int) x - 4, (int) y - 4, 8, 8);
            }

            g2.setColor(new Color(75, 85, 99));
            for (int i = 0; i < labels.size(); i++) {
                int x = left + (chartWidth * i) / Math.max(labels.size() - 1, 1);
                g2.drawString(labels.get(i), Math.max(10, x - 20), height - 12);
            }

            g2.dispose();
        }
    }

    private static final class BarChartPanel extends JPanel {
        private List<String> labels = List.of();
        private List<Integer> values = List.of();

        private BarChartPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        }

        private void setData(Map<String, Integer> data) {
            if (data == null || data.isEmpty()) {
                labels = List.of();
                values = List.of();
                return;
            }
            labels = new ArrayList<>(data.keySet());
            values = new ArrayList<>(data.values());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);

            if (values.isEmpty()) {
                g2.setColor(new Color(100, 116, 139));
                g2.drawString("No data available", width / 2 - 50, height / 2);
                g2.dispose();
                return;
            }

            int padding = 28;
            int barAreaHeight = height - 60;
            int barWidth = Math.max(42, (width - padding * 2) / (values.size() * 2));
            int gap = barWidth;
            int maxValue = Math.max(values.stream().mapToInt(Integer::intValue).max().orElse(1), 1);
            int x = padding;

            for (int i = 0; i < values.size(); i++) {
                int barHeight = Math.max(20, (int) ((barAreaHeight * (double) values.get(i)) / maxValue));
                int barX = x;
                int barY = height - 38 - barHeight;

                g2.setColor(new Color(209, 213, 219));
                g2.fillRoundRect(barX, barY, barWidth, barHeight, 12, 12);
                g2.setColor(new Color(55, 65, 81));
                g2.drawRoundRect(barX, barY, barWidth, barHeight, 12, 12);

                g2.setColor(new Color(51, 65, 85));
                g2.drawString(labels.get(i), barX - 6, height - 18);
                g2.drawString(String.valueOf(values.get(i)), barX + 4, barY - 6);

                x += barWidth + gap;
            }

            g2.dispose();
        }
    }

    private static final class ContainerUtil {
        private static void forEach(java.awt.Container container, Consumer<java.awt.Component> consumer) {
            for (java.awt.Component component : container.getComponents()) {
                consumer.accept(component);
                if (component instanceof java.awt.Container child) {
                    forEach(child, consumer);
                }
            }
        }
    }
}
