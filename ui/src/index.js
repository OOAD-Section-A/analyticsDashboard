import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.js';

/**
 * React application entry point
 * Renders the App component to the DOM
 */
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
