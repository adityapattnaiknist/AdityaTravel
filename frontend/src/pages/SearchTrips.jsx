import React from 'react';
import { Link } from 'react-router-dom';
import { api } from '../services/api';

export default function SearchTrips() {
  const [q, setQ] = React.useState({ source: '', destination: '', date: '' });
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState('');
  const [trips, setTrips] = React.useState([]);
  const [sources, setSources] = React.useState([]);
  const [destinations, setDestinations] = React.useState([]);

  React.useEffect(() => {
    // default date to today
    if (!q.date) {
      const today = new Date();
      const ds = today.toISOString().slice(0, 10);
      setQ((prev) => ({ ...prev, date: ds }));
    }
    // load routes to populate dropdowns
    (async () => {
      try {
        const routes = await api.routes.list();
        const srcSet = new Set();
        const dstSet = new Set();
        (routes || []).forEach(r => { if (r?.source) srcSet.add(r.source); if (r?.destination) dstSet.add(r.destination); });
        setSources(Array.from(srcSet).sort());
        setDestinations(Array.from(dstSet).sort());
      } catch (_) {
        // ignore; user can still type, but we use selects only
      }
    })();
  }, []);

  const onSearch = async () => {
    setError(''); setLoading(true);
    try {
      const data = await api.trips.search(q);
      const list = Array.isArray(data) ? data : [];
      setTrips(list);
    } catch (err) {
      setError(err?.response?.data?.message || err?.message || 'Search failed');
    } finally {
      setLoading(false);
    }
  };

  const canSearch = q.source && q.destination && q.date;

  return (
    <div>
      <h3>Search Trips</h3>
      <div className="row g-2 align-items-end mb-3">
        <div className="col">
          <label className="form-label">Source</label>
          <select className="form-select" value={q.source} onChange={(e)=>setQ({...q, source: e.target.value})}>
            <option value="">Select source</option>
            {sources.map(s => <option key={s} value={s}>{s}</option>)}
          </select>
        </div>
        <div className="col">
          <label className="form-label">Destination</label>
          <select className="form-select" value={q.destination} onChange={(e)=>setQ({...q, destination: e.target.value})}>
            <option value="">Select destination</option>
            {destinations.map(d => <option key={d} value={d}>{d}</option>)}
          </select>
        </div>
        <div className="col">
          <label className="form-label">Date</label>
          <input className="form-control" type="date" value={q.date} onChange={(e)=>setQ({...q, date: e.target.value})} />
        </div>
        <div className="col-auto">
          <button className="btn btn-primary" onClick={onSearch} disabled={loading || !canSearch}>{loading? 'Searching...' : 'Search'}</button>
        </div>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      {trips.length > 0 && (
        <div className="table-responsive">
          <table className="table table-hover align-middle">
            <thead className="table-light">
              <tr>
                <th>Operator</th>
                <th>From</th>
                <th>To</th>
                <th>Departure</th>
                <th>Fare</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {trips.map((t) => (
                <tr key={t.id}>
                  <td>{t.bus?.operator || '-'}</td>
                  <td>{t.route?.source}</td>
                  <td>{t.route?.destination}</td>
                  <td>{(t.departureTime || '').toString().slice(0,16).replace('T',' ')}</td>
                  <td>{t.fare != null ? `₹${t.fare}` : '-'}</td>
                  <td className="text-end"><Link className="btn btn-sm btn-outline-primary" to={`/trips/${t.id}/seats`}>Select Seats</Link></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
