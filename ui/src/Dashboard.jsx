import React, { useEffect, useMemo, useState } from 'react';
import './Dashboard.css';

const resolveApiBase = () => {
  if (window.location.hostname === 'localhost' && window.location.port && window.location.port !== '8080') {
    return 'http://localhost:8080';
  }
  return window.location.origin;
};

const API_BASE = resolveApiBase();
const API_URL = `${API_BASE}/api/dashboard`;
const REPORT_URL = `${API_BASE}/api/dashboard/report`;
const TABLE_URL = `${API_BASE}/api/dashboard/table-view`;

const formatCurrency = (value) =>
  typeof value === 'number' ? `$${value.toLocaleString(undefined, { maximumFractionDigits: 2 })}` : 'N/A';

const formatNumber = (value) =>
  typeof value === 'number' ? value.toLocaleString(undefined, { maximumFractionDigits: 2 }) : 'N/A';

const buildLineChartPoints = (values, width, height, padding) => {
  if (!values || values.length === 0) {
    return '';
  }

  if (values.length === 1) {
    const x = width / 2;
    const y = height / 2;
    return `${x},${y}`;
  }

  const maxValue = Math.max(...values, 1);
  const usableWidth = width - padding * 2;
  const usableHeight = height - padding * 2;

  return values
    .map((value, index) => {
      const x = padding + (usableWidth * index) / (values.length - 1);
      const y = padding + usableHeight - (usableHeight * value) / maxValue;
      return `${x},${y}`;
    })
    .join(' ');
};

const buildBarChartScale = (values, maxHeight) => {
  if (!values || values.length === 0) {
    return [];
  }

  const maxValue = Math.max(...values, 1);
  return values.map((value) => Math.max(12, (value / maxValue) * maxHeight));
};

const DEFAULT_FILTERS = {
  showKPIs: true,
  showAlerts: true,
  showInsights: true,
  showCharts: true,
  showRevenueTable: true,
  showInventoryTable: true,
  showDataExplorer: true,
  showReport: true,
  showMargin: true
};

const Dashboard = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [exportFormat, setExportFormat] = useState('txt');
  const [filters, setFilters] = useState(DEFAULT_FILTERS);
  const [tableDataset, setTableDataset] = useState('sales');
  const [tableView, setTableView] = useState(null);
  const [tableLoading, setTableLoading] = useState(false);
  const [tableError, setTableError] = useState(null);
  const [selectedColumns, setSelectedColumns] = useState([]);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const response = await fetch(API_URL);
        if (!response.ok) {
          throw new Error(`Dashboard API returned ${response.status}: ${response.statusText}`);
        }

        const jsonData = await response.json();
        setData(jsonData);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Unable to load dashboard');
      } finally {
        setLoading(false);
      }
    };

    loadDashboard();
  }, []);

  useEffect(() => {
    const loadTableView = async () => {
      if (!filters.showDataExplorer) {
        return;
      }

      try {
        setTableLoading(true);
        setTableError(null);
        const response = await fetch(`${TABLE_URL}?dataset=${encodeURIComponent(tableDataset)}`);
        if (!response.ok) {
          throw new Error(`Table view failed: ${response.status} ${response.statusText}`);
        }

        const jsonData = await response.json();
        setTableView(jsonData);
        setSelectedColumns(jsonData.columns || []);
      } catch (err) {
        setTableError(err instanceof Error ? err.message : 'Unable to load table view');
        setTableView(null);
      } finally {
        setTableLoading(false);
      }
    };

    loadTableView();
  }, [tableDataset, filters.showDataExplorer]);

  const handleFilterChange = (filterKey) => {
    setFilters((prev) => ({
      ...prev,
      [filterKey]: !prev[filterKey]
    }));
  };

  const setAllFilters = (enabled) => {
    setFilters({
      showKPIs: enabled,
      showAlerts: enabled,
      showInsights: enabled,
      showCharts: enabled,
      showRevenueTable: enabled,
      showInventoryTable: enabled,
      showDataExplorer: enabled,
      showReport: enabled,
      showMargin: enabled
    });
  };

  const selectedSections = useMemo(() => {
    const sections = [];
    if (filters.showKPIs) sections.push('kpis');
    if (filters.showAlerts) sections.push('alerts');
    if (filters.showInsights) sections.push('insights');
    if (filters.showCharts || filters.showRevenueTable || filters.showInventoryTable) sections.push('visualizations');
    if (filters.showReport) sections.push('report');
    if (filters.showMargin) sections.push('margin');
    sections.push('reporting');
    return sections;
  }, [filters]);

  const downloadReport = async () => {
    try {
      const params = new URLSearchParams({
        format: exportFormat,
        sections: selectedSections.join(',')
      });

      const response = await fetch(`${REPORT_URL}?${params.toString()}`);
      if (!response.ok) {
        throw new Error(`Report export failed: ${response.status} ${response.statusText}`);
      }

      const content = await response.text();
      const blob = new Blob([content], { type: response.headers.get('content-type') || 'text/plain' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `analytics-dashboard-report.${exportFormat}`;
      link.click();
      URL.revokeObjectURL(url);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unable to export report');
    }
  };

  if (loading) {
    return (
      <div className="dashboard loading">
        <div className="loader">Loading dashboard data...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="dashboard error">
        <div className="error-box">
          <h2>Error loading dashboard</h2>
          <p>{error}</p>
          <p className="hint">Make sure the backend server is running on http://localhost:8080</p>
        </div>
      </div>
    );
  }

  if (!data) {
    return (
      <div className="dashboard error">
        <div className="error-box">
          <h2>No data available</h2>
        </div>
      </div>
    );
  }

  const revenueByProduct = Object.entries(data.visualizations?.revenueByProduct || {});
  const inventoryLevels = Object.entries(data.visualizations?.inventoryLevels || {});
  const demandTrendSeries = revenueByProduct.slice(0, 6);
  const demandTrendValues = demandTrendSeries.map(([, value]) => Number(value) || 0);
  const demandTrendPoints = buildLineChartPoints(demandTrendValues, 360, 160, 18);
  const demandTrendLabels = demandTrendSeries.map(([label]) => label);

  const supplierPerformanceSeries = [
    {
      label: 'Reliability',
      value: Number(data.kpis?.avgSupplierReliability) || 0
    },
    {
      label: 'On-time',
      value: Number(data.kpis?.onTimeShipmentRate) || 0
    },
    {
      label: 'Completion',
      value: Number(data.kpis?.orderCompletionRate) || 0
    }
  ];
  const supplierPerformanceHeights = buildBarChartScale(
    supplierPerformanceSeries.map((item) => item.value),
    96
  );

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <div className="header-content">
          <h1>Analytics Dashboard</h1>
          <p className="subtitle">Supply chain reporting, analytics, and export controls</p>
        </div>
        <div className="header-controls">
          <label className="export-format">
            <span>Format</span>
            <select value={exportFormat} onChange={(event) => setExportFormat(event.target.value)}>
              <option value="txt">Text</option>
              <option value="csv">CSV</option>
              <option value="json">JSON</option>
            </select>
          </label>
          <button className="btn btn-export" onClick={downloadReport} title="Download report">
            Download report
          </button>
        </div>
      </header>

      <section className="filter-panel">
        <div className="filter-panel__title">
          <h3>View filters</h3>
          <p>Select the sections you want to see on screen and include in the download.</p>
        </div>
        <div className="filter-actions">
          <button type="button" className="filter-action" onClick={() => setAllFilters(true)}>
            Show all
          </button>
          <button type="button" className="filter-action" onClick={() => setAllFilters(false)}>
            Clear all
          </button>
        </div>
        <div className="filter-options">
          <label className="filter-checkbox">
            <input type="checkbox" checked={filters.showKPIs} onChange={() => handleFilterChange('showKPIs')} />
            <span>KPI cards</span>
          </label>
          <label className="filter-checkbox">
            <input type="checkbox" checked={filters.showAlerts} onChange={() => handleFilterChange('showAlerts')} />
            <span>Alerts</span>
          </label>
          <label className="filter-checkbox">
            <input type="checkbox" checked={filters.showInsights} onChange={() => handleFilterChange('showInsights')} />
            <span>Insights</span>
          </label>
          <label className="filter-checkbox">
            <input type="checkbox" checked={filters.showCharts} onChange={() => handleFilterChange('showCharts')} />
            <span>Charts</span>
          </label>
          <label className="filter-checkbox">
            <input
              type="checkbox"
              checked={filters.showRevenueTable}
              onChange={() => handleFilterChange('showRevenueTable')}
            />
            <span>Revenue table</span>
          </label>
          <label className="filter-checkbox">
            <input
              type="checkbox"
              checked={filters.showInventoryTable}
              onChange={() => handleFilterChange('showInventoryTable')}
            />
            <span>Inventory table</span>
          </label>
          <label className="filter-checkbox">
            <input
              type="checkbox"
              checked={filters.showDataExplorer}
              onChange={() => handleFilterChange('showDataExplorer')}
            />
            <span>Data explorer</span>
          </label>
          <label className="filter-checkbox">
            <input type="checkbox" checked={filters.showReport} onChange={() => handleFilterChange('showReport')} />
            <span>Report</span>
          </label>
          <label className="filter-checkbox">
            <input type="checkbox" checked={filters.showMargin} onChange={() => handleFilterChange('showMargin')} />
            <span>Profitability</span>
          </label>
        </div>
      </section>

      {filters.showKPIs && (
        <section className="section">
          <h2>Key Performance Indicators</h2>
          <div className="kpi-cards">
            <div className="kpi-card revenue">
              <h3>Total Revenue</h3>
              <p className="value">{formatCurrency(data.kpis?.totalRevenue)}</p>
            </div>
            <div className="kpi-card orders">
              <h3>Total Orders</h3>
              <p className="value">{formatNumber(data.kpis?.totalOrders)}</p>
            </div>
            <div className="kpi-card inventory">
              <h3>Inventory Units</h3>
              <p className="value">{formatNumber(data.kpis?.totalInventoryUnits)}</p>
            </div>
            <div className="kpi-card shipment">
              <h3>On-Time Shipments</h3>
              <p className="value">{formatNumber(data.kpis?.onTimeShipmentRate)}%</p>
            </div>
            <div className="kpi-card warehouse">
              <h3>Warehouse Utilization</h3>
              <p className="value">{formatNumber(data.kpis?.avgWarehouseUtilization)}%</p>
            </div>
            <div className="kpi-card forecast">
              <h3>Forecast Accuracy</h3>
              <p className="value">{formatNumber(data.kpis?.forecastAccuracyRate)}%</p>
            </div>
            <div className="kpi-card pending">
              <h3>Pending Orders</h3>
              <p className="value">{formatNumber(data.kpis?.pendingOrders)}</p>
            </div>
            <div className="kpi-card delayed">
              <h3>Delayed Shipments</h3>
              <p className="value">{formatNumber(data.kpis?.delayedShipments)}</p>
            </div>
          </div>
        </section>
      )}

      {filters.showCharts && (
        <section className="section analytics-section">
          <h2>Analytics</h2>
          <div className="analytics-grid">
            <article className="analytics-card">
              <div className="analytics-card__header">
                <h3>Demand Trend</h3>
                <p>Line chart view of revenue by product from the latest dataset.</p>
              </div>
              <div className="chart-frame">
                {demandTrendSeries.length > 0 ? (
                  <svg viewBox="0 0 360 160" className="chart-svg" role="img" aria-label="Demand trend line chart">
                    <defs>
                      <linearGradient id="demandTrendFill" x1="0" x2="0" y1="0" y2="1">
                        <stop offset="0%" stopColor="#4f46e5" stopOpacity="0.35" />
                        <stop offset="100%" stopColor="#4f46e5" stopOpacity="0.05" />
                      </linearGradient>
                    </defs>
                    <polyline
                      points={demandTrendPoints}
                      className="chart-line"
                    />
                    <polyline
                      points={`18,142 ${demandTrendPoints} 342,142`}
                      className="chart-area"
                    />
                    {demandTrendValues.map((value, index) => {
                      const usableWidth = 360 - 36;
                      const usableHeight = 160 - 36;
                      const maxValue = Math.max(...demandTrendValues, 1);
                      const x = 18 + (usableWidth * index) / Math.max(demandTrendValues.length - 1, 1);
                      const y = 18 + usableHeight - (usableHeight * value) / maxValue;
                      return <circle key={`${demandTrendLabels[index]}-${index}`} cx={x} cy={y} r="4" className="chart-point" />;
                    })}
                  </svg>
                ) : (
                  <p className="no-data">No revenue data available</p>
                )}
              </div>
              <div className="analytics-legend">
                {demandTrendSeries.map(([label, value]) => (
                  <div key={label} className="legend-item">
                    <span className="legend-dot" />
                    <span className="legend-label">{label}</span>
                    <strong>{formatCurrency(Number(value) || 0)}</strong>
                  </div>
                ))}
              </div>
            </article>

            <article className="analytics-card">
              <div className="analytics-card__header">
                <h3>Supplier Performance</h3>
                <p>Bar chart view of supplier-related performance indicators.</p>
              </div>
              <div className="bar-chart">
                {supplierPerformanceSeries.map((item, index) => (
                  <div key={item.label} className="bar-item">
                    <div className="bar-track">
                      <div
                        className="bar-fill"
                        style={{ height: `${supplierPerformanceHeights[index]}px` }}
                        title={`${item.label}: ${formatNumber(item.value)}%`}
                      />
                    </div>
                    <span className="bar-label">{item.label}</span>
                    <strong className="bar-value">{formatNumber(item.value)}%</strong>
                  </div>
                ))}
              </div>
            </article>
          </div>
        </section>
      )}

      {filters.showAlerts && (
        <section className="section alerts-section">
          <h2>System Alerts</h2>
          {data.alerts && data.alerts.length > 0 ? (
            <ul className="alerts">
              {data.alerts.map((alert, index) => (
                <li key={index} className="alert-item">
                  {alert}
                </li>
              ))}
            </ul>
          ) : (
            <p className="no-data">No active alerts</p>
          )}
        </section>
      )}

      {filters.showInsights && (
        <section className="section insights-section">
          <h2>Business Insights</h2>
          {data.insights && data.insights.length > 0 ? (
            <ul className="insights">
              {data.insights.map((insight, index) => (
                <li key={index} className="insight-item">
                  {insight}
                </li>
              ))}
            </ul>
          ) : (
            <p className="no-data">All metrics are within acceptable ranges</p>
          )}
        </section>
      )}

      {filters.showCharts && filters.showRevenueTable && revenueByProduct.length > 0 && (
        <section className="section">
          <h2>Revenue by Product</h2>
          <div className="data-table">
            <table>
              <thead>
                <tr>
                  <th>Product</th>
                  <th>Revenue</th>
                </tr>
              </thead>
              <tbody>
                {revenueByProduct.map(([product, revenue], index) => (
                  <tr key={index}>
                    <td>{product}</td>
                    <td>{formatCurrency(revenue)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      )}

      {filters.showCharts && filters.showInventoryTable && inventoryLevels.length > 0 && (
        <section className="section">
          <h2>Inventory Levels</h2>
          <div className="data-table">
            <table>
              <thead>
                <tr>
                  <th>Product</th>
                  <th>Units</th>
                </tr>
              </thead>
              <tbody>
                {inventoryLevels.map(([product, units], index) => (
                  <tr key={index}>
                    <td>{product}</td>
                    <td>{formatNumber(units)} units</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      )}

      {filters.showDataExplorer && (
        <section className="section explorer-section">
          <div className="explorer-header">
            <div>
              <h2>Data Explorer</h2>
              <p>Pick a dataset, then choose which columns you want to show.</p>
            </div>
            <label className="dataset-picker">
              <span>Dataset</span>
              <select value={tableDataset} onChange={(event) => setTableDataset(event.target.value)}>
                <option value="sales">Sales</option>
                <option value="inventory">Inventory</option>
                <option value="orders">Orders</option>
                <option value="shipments">Shipments</option>
                <option value="warehouses">Warehouses</option>
                <option value="suppliers">Suppliers</option>
                <option value="forecasts">Forecasts</option>
              </select>
            </label>
          </div>

          {tableLoading ? (
            <p className="no-data">Loading table data...</p>
          ) : tableError ? (
            <p className="no-data">{tableError}</p>
          ) : tableView ? (
            <>
              <div className="column-picker">
                {tableView.columns?.map((column) => (
                  <label key={column} className="filter-checkbox">
                    <input
                      type="checkbox"
                      checked={selectedColumns.includes(column)}
                      onChange={() =>
                        setSelectedColumns((prev) =>
                          prev.includes(column) ? prev.filter((item) => item !== column) : [...prev, column]
                        )
                      }
                    />
                    <span>{column}</span>
                  </label>
                ))}
              </div>

              <div className="data-table data-table--wide">
                <table>
                  <thead>
                    <tr>
                      {selectedColumns.map((column) => (
                        <th key={column}>{column}</th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {tableView.rows?.length > 0 ? (
                      tableView.rows.map((row, rowIndex) => (
                        <tr key={rowIndex}>
                          {selectedColumns.map((column) => (
                            <td key={`${rowIndex}-${column}`}>{String(row[column] ?? '')}</td>
                          ))}
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan={selectedColumns.length || 1} className="no-data">
                          No rows available
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </>
          ) : (
            <p className="no-data">No table data loaded</p>
          )}
        </section>
      )}

      {filters.showReport && data.report && (
        <section className="section report-section">
          <h2>Report Summary</h2>
          <div className="report-content">
            <p>{data.report.summary}</p>
            <p className="generated-at">
              Generated at: {new Date(data.report.generatedAt).toLocaleString()}
            </p>
          </div>
        </section>
      )}

      {filters.showMargin && data.marginResult && (
        <section className="section margin-section">
          <h2>Margin Profitability</h2>
          <div className="margin-data">
            <div className="margin-item">
              <h3>Margin Conceded</h3>
              <p>{formatCurrency(data.marginResult.marginConceded)}</p>
            </div>
            <div className="margin-item">
              <h3>Margin Protected</h3>
              <p>{formatCurrency(data.marginResult.marginProtected)}</p>
            </div>
          </div>
        </section>
      )}

      <footer className="dashboard-footer">
        <p>Analytics Dashboard v1.0.0 | Last updated: {new Date().toLocaleString()}</p>
      </footer>
    </div>
  );
};

export default Dashboard;
