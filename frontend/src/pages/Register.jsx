import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../services/api';

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = React.useState({ name: '', email: '', phone: '', password: '' });
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState('');
  const [ok, setOk] = React.useState('');

  const onSubmit = async (e) => {
    e.preventDefault();
    setError(''); setOk(''); setLoading(true);
    try {
      await api.auth.register(form);
      setOk('Account created. You can now login.');
      setTimeout(() => navigate('/login'), 800);
    } catch (err) {
      setError(err?.response?.data?.message || err?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="col-md-5 mx-auto">
      <h3>User Registration</h3>
      {ok && <div className="alert alert-success">{ok}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={onSubmit} className="row g-2">
        <div className="col-md-6">
          <label className="form-label">Name</label>
          <input className="form-control" value={form.name} onChange={(e)=>setForm({...form, name: e.target.value})} required />
        </div>
        <div className="col-md-6">
          <label className="form-label">Email</label>
          <input className="form-control" type="email" value={form.email} onChange={(e)=>setForm({...form, email: e.target.value})} required />
        </div>
        <div className="col-md-6">
          <label className="form-label">Phone</label>
          <input className="form-control" value={form.phone} onChange={(e)=>setForm({...form, phone: e.target.value})} />
        </div>
        <div className="col-md-6">
          <label className="form-label">Password</label>
          <input className="form-control" type="password" value={form.password} onChange={(e)=>setForm({...form, password: e.target.value})} required />
        </div>
        <div className="col-12">
          <button className="btn btn-success w-100" disabled={loading}>{loading? 'Creating...' : 'Create Account'}</button>
        </div>
      </form>
      <div className="mt-2 text-center">
        <small>Already have an account? <Link to="/login">Login</Link></small>
      </div>
    </div>
  );
}
