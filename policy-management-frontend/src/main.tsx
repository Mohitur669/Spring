import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ErrorBoundary } from './components/common/ErrorBoundary';
import { Layout } from './components/layout/Layout';
import { Dashboard } from './pages/Dashboard';
import { PolicyList } from './pages/PolicyList';
import { PolicyDetails } from './pages/PolicyDetails';
import { CreatePolicy } from './pages/CreatePolicy';
import './index.css';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <ErrorBoundary>
      <BrowserRouter>
        <Layout>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/policies" element={<PolicyList />} />
            <Route path="/policies/create" element={<CreatePolicy />} />
            <Route path="/policies/:policyNumber" element={<PolicyDetails />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Layout>
      </BrowserRouter>
    </ErrorBoundary>
  </React.StrictMode>,
);
