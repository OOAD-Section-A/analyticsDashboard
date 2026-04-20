import React, { useState, useEffect } from 'react';

const Dashboard = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch('/api/dashboard')
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch dashboard data');
        }
        return response.json();
      })
      .then(data => {
        setData(data);
        setLoading(false);
      })
      .catch(error => {
        setError(error.message);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div className="dashboard">Loading dashboard data...</div>;
  }

  if (error) {
    return <div className="dashboard">Error: {error}</div>;
  }

  return (
    <div className="dashboard">
      <h1>Analytics Dashboard</h1>

      {/* KPI Cards */}
      <div className="kpi-cards">
        <div className="kpi-card">
          <h3>Total Revenue</h3>
          <p>${data.kpis.totalRevenue?.toLocaleString() || 'N/A'}</p>
        </div>
        <div className="kpi-card">
          <h3>Total Orders</h3>
          <p>{data.kpis.totalOrders?.toLocaleString() || 'N/A'}</p>
        </div>
        <div className="kpi-card">
          <h3>Inventory Value</h3>
          <p>${data.kpis.inventoryValue?.toLocaleString() || 'N/A'}</p>
        </div>
      </div>

      {/* Alerts */}
      <div className="section">
        <h2>Alerts</h2>
        <ul className="alerts">
          {data.alerts?.map((alert, index) => (
            <li key={index}>{alert}</li>
          )) || <li>No alerts</li>}
        </ul>
      </div>

      {/* Insights */}
      <div className="section">
        <h2>Insights</h2>
        <ul className="insights">
          {data.insights?.map((insight, index) => (
            <li key={index}>{insight}</li>
          )) || <li>No insights</li>}
        </ul>
      </div>

      {/* Report Summary */}
      <div className="section">
        <h2>Report Summary</h2>
        <div className="report-summary">
          {data.report?.summary || 'No summary available'}
        </div>
      </div>

      {/* Simple Chart Placeholder */}
      <div className="section">
        <h2>Visualizations</h2>
        <div className="chart-placeholder">
          Chart visualization would go here (simple placeholder)
        </div>
      </div>
    </div>
  );
};

export default Dashboard;