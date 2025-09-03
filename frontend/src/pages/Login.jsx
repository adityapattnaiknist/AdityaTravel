import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { api } from '../services/api';

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [tab, setTab] = React.useState('USER');
  const [form, setForm] = React.useState({ email: '', password: '' });
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState('');

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await api.auth.login(form.email, form.password);
      const userObj = res?.user || { id: res?.userId, email: form.email, name: res?.name || form.email, role: res?.role || 'CUSTOMER' };
      const token = '';
      // If admin tab chosen but user isn't admin, block
      if (tab === 'ADMIN' && userObj.role !== 'ADMIN') {
        throw new Error('Not an admin user');
      }
      login(token, userObj);
      if (userObj.role === 'ADMIN') navigate('/admin/trips', { replace: true });
      else navigate('/search', { replace: true });
    } catch (err) {
      setError(err?.response?.data?.message || err?.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="col-md-5 mx-auto">
      <h3 className="mb-3">Login</h3>
      <ul className="nav nav-tabs mb-3">
        <li className="nav-item">
          <button className={`nav-link ${tab==='USER'?'active':''}`} onClick={()=>setTab('USER')}>User</button>
        </li>
        <li className="nav-item">
          <button className={`nav-link ${tab==='ADMIN'?'active':''}`} onClick={()=>setTab('ADMIN')}>Admin</button>
        </li>
      </ul>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={onSubmit}>
        <input className="form-control mb-2" placeholder="Email" type="email" value={form.email} onChange={(e)=>setForm({...form, email: e.target.value})} required />
        <input className="form-control mb-3" placeholder="Password" type="password" value={form.password} onChange={(e)=>setForm({...form, password: e.target.value})} required />
        <button className="btn btn-primary w-100" disabled={loading}>{loading? 'Signing in...' : (tab==='ADMIN'?'Login as Admin':'Login')}</button>
      </form>
      {tab==='USER' && (
        <div className="mt-2 text-center">
          <small>No account? <Link to="/register">Register</Link></small>
        </div>
      )}
    </div>
  );
}
