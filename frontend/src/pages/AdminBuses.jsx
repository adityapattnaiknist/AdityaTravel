import React from 'react';
import { api } from '../services/api';

export default function AdminBuses() {
  const [items, setItems] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState('');
  const [form, setForm] = React.useState({ busNumber:'', busType:'', operator:'', totalSeats:'', seatLayoutJson:'' });
  const [editing, setEditing] = React.useState(null);

  const load = async () => {
    setLoading(true); setError('');
    try { setItems(await api.buses.list()); } catch (e) { setError(e?.message || 'Failed'); } finally { setLoading(false); }
  };
  React.useEffect(() => { load(); }, []);

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = { ...form, totalSeats: form.totalSeats? Number(form.totalSeats): null };
      if (editing) await api.buses.update(editing, payload); else await api.buses.create(payload);
      setForm({ busNumber:'', busType:'', operator:'', totalSeats:'', seatLayoutJson:'' }); setEditing(null); await load();
    } catch (e) { alert(e?.response?.data?.message || e?.message); }
  };

  const edit = (it) => { setEditing(it.id); setForm({ busNumber: it.busNumber||'', busType: it.busType||'', operator: it.operator||'', totalSeats: it.totalSeats||'', seatLayoutJson: it.seatLayoutJson||'' }); };
  const del = async (id) => { if (!window.confirm('Delete bus?')) return; await api.buses.remove(id); await load(); };

  return (
    <div>
      <h3>Buses</h3>
      <div className="mb-2 fw-semibold">Add / Edit Bus</div>
      <form onSubmit={onSubmit} className="row g-2 mb-3">
        <div className="col-md-2"><input placeholder="Bus Number" className="form-control" value={form.busNumber} onChange={(e)=>setForm({...form, busNumber:e.target.value})} required /></div>
        <div className="col-md-2"><input placeholder="Type" className="form-control" value={form.busType} onChange={(e)=>setForm({...form, busType:e.target.value})} /></div>
        <div className="col-md-2"><input placeholder="Operator" className="form-control" value={form.operator} onChange={(e)=>setForm({...form, operator:e.target.value})} /></div>
        <div className="col-md-2"><input placeholder="Total Seats" type="number" className="form-control" value={form.totalSeats} onChange={(e)=>setForm({...form, totalSeats:e.target.value})} /></div>
        <div className="col-12"><textarea placeholder='Seat Layout JSON (optional): {"seats":[{"num":"A1","type":"WINDOW"}]}' className="form-control" rows={3} value={form.seatLayoutJson} onChange={(e)=>setForm({...form, seatLayoutJson:e.target.value})} /></div>
        <div className="col-12 d-flex justify-content-end"><button className="btn btn-primary">{editing? 'Update':'Add'} Bus</button></div>
      </form>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? 'Loading...' : (
        <div className="table-responsive">
          <table className="table table-striped align-middle">
            <thead><tr><th>Number</th><th>Type</th><th>Operator</th><th>Total Seats</th><th></th></tr></thead>
            <tbody>
              {items.map(it => (
                <tr key={it.id}>
                  <td>{it.busNumber}</td>
                  <td>{it.busType}</td>
                  <td>{it.operator}</td>
                  <td>{it.totalSeats ?? '-'}</td>
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
