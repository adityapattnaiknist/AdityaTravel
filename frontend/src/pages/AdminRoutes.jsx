import React from 'react';
import { api } from '../services/api';

export default function AdminRoutes() {
  const [items, setItems] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState('');
  const [form, setForm] = React.useState({ source: '', destination: '', distance: '', hours: '', minutes: '' });
  const [editing, setEditing] = React.useState(null);

  const load = async () => {
    setLoading(true); setError('');
    try { setItems(await api.routes.list()); } catch (e) { setError(e?.message || 'Failed'); } finally { setLoading(false); }
  };
  React.useEffect(() => { load(); }, []);

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      const duration = (Number(form.hours||0) * 60) + Number(form.minutes||0);
      const payload = { source: form.source, destination: form.destination, distance: Number(form.distance||0), duration };
      if (editing) await api.routes.update(editing, payload); else await api.routes.create(payload);
      setForm({ source: '', destination: '', distance: '', hours: '', minutes: '' }); setEditing(null); await load();
    } catch (e) { alert(e?.response?.data?.message || e?.message); }
  };

  const edit = (it) => {
    setEditing(it.id);
    const mins = Number(it.duration||0);
    const hours = Math.floor(mins / 60);
    const minutes = mins % 60;
    setForm({ source: it.source, destination: it.destination, distance: it.distance||'', hours: hours||'', minutes: minutes||'' });
  };
  const del = async (id) => { if (!window.confirm('Delete route?')) return; await api.routes.remove(id); await load(); };

  return (
    <div>
      <h3>Routes</h3>
      <div className="mb-2 fw-semibold">Add / Edit Route</div>
      <form onSubmit={onSubmit} className="row g-2 mb-3">
        <div className="col-md-3"><input placeholder="Source" className="form-control" value={form.source} onChange={(e)=>setForm({...form, source:e.target.value})} required /></div>
        <div className="col-md-3"><input placeholder="Destination" className="form-control" value={form.destination} onChange={(e)=>setForm({...form, destination:e.target.value})} required /></div>
        <div className="col-md-2"><input placeholder="Distance (km)" className="form-control" type="number" value={form.distance} onChange={(e)=>setForm({...form, distance:e.target.value})} /></div>
        <div className="col-md-2">
          <div className="input-group">
            <input placeholder="Hours" className="form-control" type="number" min="0" value={form.hours} onChange={(e)=>setForm({...form, hours:e.target.value})} />
            <span className="input-group-text">hrs</span>
            <input placeholder="Minutes" className="form-control" type="number" min="0" max="59" value={form.minutes} onChange={(e)=>setForm({...form, minutes:e.target.value})} />
            <span className="input-group-text">mins</span>
          </div>
        </div>
        <div className="col-md-2 d-grid"><button className="btn btn-primary">{editing? 'Update':'Add'} Route</button></div>
      </form>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? 'Loading...' : (
        <div className="table-responsive">
          <table className="table table-striped align-middle">
            <thead><tr><th>Source</th><th>Destination</th><th>Distance</th><th>Duration</th><th></th></tr></thead>
            <tbody>
              {items.map(it => (
                <tr key={it.id}>
                  <td>{it.source}</td>
                  <td>{it.destination}</td>
                  <td>{it.distance ?? '-'}</td>
                  <td>{formatDuration(it.duration)}</td>
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

function formatDuration(mins) {
  const m = Number(mins||0);
  const h = Math.floor(m/60);
  const r = m%60;
  if (!m) return '-';
  if (h && r) return `${h} hrs ${r} mins`;
  if (h) return `${h} hrs`;
  return `${r} mins`;
}
