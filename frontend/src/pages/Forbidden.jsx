import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Forbidden() {
  const { user } = useAuth();
  const dest = user?.role === 'ADMIN' ? '/admin/trips' : '/search';
  const label = user?.role === 'ADMIN' ? 'Go to Admin' : 'Go to Search';
  return (
    <div className="col-md-6 mx-auto text-center">
      <h3>403 - Forbidden</h3>
      <p className="text-muted">You do not have access to this page.</p>
      <Link to={user ? dest : '/login'} className="btn btn-primary">
        {user ? label : 'Login'}
      </Link>
    </div>
  );
}

