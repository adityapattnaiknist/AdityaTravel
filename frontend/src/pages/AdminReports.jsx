import React from 'react';
import { api } from '../services/api';

export default function AdminReports() {
  const [data, setData] = React.useState(null);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState('');

  React.useEffect(() => {
    (async () => {
      setLoading(true); setError('');
      try { setData(await api.reports.sales()); } catch (e) { setError(e?.message || 'Failed'); } finally { setLoading(false); }
    })();
  }, []);

  return (
    <div>
      <h3>Sales Summary</h3>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? 'Loading...' : (
        <div className="row g-3">
          <div className="col-md-4">
            <div className="card card-body">
              <div className="text-muted">Total Sales</div>
              <div className="fs-4 fw-semibold">₹{data?.totalSales ?? 0}</div>
            </div>
          </div>
          <div className="col-md-4">
            <div className="card card-body">
              <div className="text-muted">Successful Payments</div>
              <div className="fs-4 fw-semibold">{data?.successfulPayments ?? 0}</div>
            </div>
          </div>
          <div className="col-md-4">
            <div className="card card-body">
              <div className="text-muted">All Payments</div>
              <div className="fs-4 fw-semibold">{data?.allPayments ?? 0}</div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

