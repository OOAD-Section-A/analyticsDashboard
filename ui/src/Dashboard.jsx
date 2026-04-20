import React, { useState, useEffect } from 'react';

const API_URL = 'http://localhost:8080/api/dashboard';

const formatCurrency = (value) =>
  typeof value === 'number' ? `$${value.toLocaleString(undefined, { maximumFractionDigits: 2 })}` : 'N/A';

const formatNumber = (value) =>
  typeof value === 'number' ? value.toLocaleString(undefined, { maximumFractionDigits: 2 }) : 'N/A';

const Dashboard = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const response = await fetch(API_URL);
        if (!response.ok) {
          throw new Error(`Dashboard API returned ${response.status}`);
        }
        setData(await response.json());
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    loadDashboard();
  }, []);

  if (loading) {
    return <div className="dashboard">Loading dashboard data...</div>;
  }

  if (error) {
    return <div className="dashboard">Error: {error}</div>;
  }

  const revenueByProduct = Object.entries(data.visualizations?.revenueByProduct || {});
  const inventoryLevels = Object.entries(data.visualizations?.inventoryLevels || {});

  return (
    <div className="dashboard">
      <h1>Analytics Dashboard</h1>

      <div className="kpi-cards">
        <div className="kpi-card">
          <h3>Total Revenue</h3>
          <p>{formatCurrency(data.kpis?.totalRevenue)}</p>
        </div>
        <div className="kpi-card">
          <h3>Total Orders</h3>
          <p>{formatNumber(data.kpis?.totalOrders)}</p>
        </div>
        <div className="kpi-card">
          <h3>Inventory Units</h3>
          <p>{formatNumber(data.kpis?.totalInventoryUnits)}</p>
        </div>
        <div className="kpi-card">
          <h3>On-Time Shipments</h3>
          <p>{formatNumber(data.kpis?.onTimeShipmentRate)}%</p>
        </div>
      </div>

      <div className="kpi-cards">
        <div className="kpi-card">
          <h3>Warehouse Utilization</h3>
          <p>{formatNumber(data.kpis?.avgWarehouseUtilization)}%</p>
        </div>
        <div className="kpi-card">
          <h3>Forecast Accuracy</h3>
          <p>{formatNumber(data.kpis?.forecastAccuracyRate)}%</p>
        </div>
        <div className="kpi-card">
          <h3>Pending Orders</h3>
          <p>{formatNumber(data.kpis?.pendingOrders)}</p>
        </div>
        <div className="kpi-card">
          <h3>Delayed Shipments</h3>
          <p>{formatNumber(data.kpis?.delayedShipments)}</p>
        </div>
      </div>

      <div className="section">
        <h2>Alerts</h2>
        <ul className="alerts">
          {data.alerts?.length ? data.alerts.map((alert, index) => (
            <li key={index}>{alert}</li>
          )) : <li>No alerts</li>}
        </ul>
      </div>

      <div className="section">
        <h2>Insights</h2>
        <ul className="insights">
          {data.insights?.length ? data.insights.map((insight, index) => (
            <li key={index}>{insight}</li>
          )) : <li>No insights</li>}
        </ul>
      </div>

      <div className="section">
        <h2>Report Summary</h2>
        <div className="report-summary">
          {data.report?.summary || 'No summary available'}
        </div>
      </div>

      <div className="section">
        <h2>Revenue by Product</h2>
        <div className="metric-list">
          {revenueByProduct.map(([productId, revenue]) => (
            <div className="metric-row" key={productId}>
              <span>{productId}</span>
              <strong>{formatCurrency(revenue)}</strong>
            </div>
          ))}
        </div>
      </div>

      <div className="section">
        <h2>Inventory Levels</h2>
        <div className="metric-list">
          {inventoryLevels.map(([productId, quantity]) => (
            <div className="metric-row" key={productId}>
              <span>{productId}</span>
              <strong>{formatNumber(quantity)} units</strong>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
