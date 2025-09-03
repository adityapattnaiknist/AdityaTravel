import React from 'react';
import { api } from '../services/api';

export default function AdminTrips() {
  const [items, setItems] = React.useState([]);
  const [routes, setRoutes] = React.useState([]);
  const [buses, setBuses] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState('');
  const [form, setForm] = React.useState({ busId:'', routeId:'', departureTime:'', arrivalTime:'', fare:'' });
  const [editing, setEditing] = React.useState(null);

  const load = async () => {
    setLoading(true); setError('');
    try {
      const [ts, rs, bs] = await Promise.all([api.trips.list(), api.routes.list(), api.buses.list()]);
      setItems(ts); setRoutes(rs); setBuses(bs);
    } catch (e) { setError(e?.message || 'Failed'); } finally { setLoading(false); }
  };
  React.useEffect(() => { load(); }, []);

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        busId: Number(form.busId),
        routeId: Number(form.routeId),
        departureTime: form.departureTime ? new Date(form.departureTime).toISOString() : null,
        arrivalTime: form.arrivalTime ? new Date(form.arrivalTime).toISOString() : null,
        fare: form.fare ? Number(form.fare) : null,
      };
      if (editing) await api.trips.update(editing, payload); else await api.trips.create(payload);
      setForm({ busId:'', routeId:'', departureTime:'', arrivalTime:'', fare:'' }); setEditing(null); await load();
    } catch (e) { alert(e?.response?.data?.message || e?.message); }
  };

  const edit = (it) => {
    setEditing(it.id);
    setForm({
      busId: it.bus?.id || '',
      routeId: it.route?.id || '',
      departureTime: it.departureTime ? it.departureTime.slice(0,16) : '',
      arrivalTime: it.arrivalTime ? it.arrivalTime.slice(0,16) : '',
      fare: it.fare ?? '',
    });
  };
  const del = async (id) => { if (!window.confirm('Delete trip?')) return; await api.trips.remove(id); await load(); };

  return (
    <div>
      <h3>Trips</h3>
      <div className="mb-2 fw-semibold">Schedule / Edit Trip</div>
      <form onSubmit={onSubmit} className="row g-2 mb-3">
        <div className="col-md-3">
          <select className="form-select" value={form.busId} onChange={(e)=>setForm({...form, busId:e.target.value})} required>
            <option value="">Select Bus</option>
            {buses.map(b => <option key={b.id} value={b.id}>{b.busNumber} - {b.operator}</option>)}
          </select>
        </div>
        <div className="col-md-3">
          <select className="form-select" value={form.routeId} onChange={(e)=>setForm({...form, routeId:e.target.value})} required>
            <option value="">Select Route</option>
            {routes.map(r => <option key={r.id} value={r.id}>{r.source} → {r.destination}</option>)}
          </select>
        </div>
        <div className="col-md-2"><input type="datetime-local" className="form-control" value={form.departureTime} onChange={(e)=>setForm({...form, departureTime:e.target.value})} required /></div>
        <div className="col-md-2"><input type="datetime-local" className="form-control" value={form.arrivalTime} onChange={(e)=>setForm({...form, arrivalTime:e.target.value})} required /></div>
        <div className="col-md-1"><input placeholder="Fare" type="number" className="form-control" value={form.fare} onChange={(e)=>setForm({...form, fare:e.target.value})} required /></div>
        <div className="col-md-1 d-grid"><button className="btn btn-primary">{editing? 'Update':'Add'}</button></div>
        <div className="col-12"><small className="text-muted">Seats are auto-generated on create using bus layout or totalSeats.</small></div>
      </form>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? 'Loading...' : (
        <div className="table-responsive">
          <table className="table table-striped align-middle">
            <thead><tr><th>Route</th><th>Bus</th><th>Departure</th><th>Arrival</th><th>Fare</th><th></th></tr></thead>
            <tbody>
              {items.map(it => (
                <tr key={it.id}>
                  <td>{it.route?.source} → {it.route?.destination}</td>
                  <td>{it.bus?.busNumber} ({it.bus?.operator})</td>
                  <td>{(it.departureTime || '').toString().slice(0,16).replace('T',' ')}</td>
                  <td>{(it.arrivalTime || '').toString().slice(0,16).replace('T',' ')}</td>
                  <td>{it.fare != null ? `₹${it.fare}` : '-'}</td>
                  <td className="text-end">
                    <button className="btn btn-sm btn-outline-secondary me-2" onClick={()=>edit(it)}>Edit</button>
                    <button className="btn btn-sm btn-outline-danger" onClick={()=>del(it.id)}>Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
