import React from 'react';
import { Link, NavLink, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const isAdmin = user?.role === 'ADMIN';
  const isAuthScreen = location.pathname === '/login' || location.pathname === '/register';

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container-fluid">
        <Link className="navbar-brand" to={isAuthScreen ? '/login' : (isAdmin ? '/admin/trips' : '/search')}>BusRes</Link>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nav" aria-controls="nav" aria-expanded="false" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="nav">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            {!isAuthScreen && user && !isAdmin && (
              <>
                <li className="nav-item"><NavLink className="nav-link" to="/search">Search</NavLink></li>
                <li className="nav-item"><NavLink className="nav-link" to="/my-tickets">My Tickets</NavLink></li>
              </>
            )}
            {!isAuthScreen && user && isAdmin && (
              <>
                <li className="nav-item"><NavLink className="nav-link" to="/admin/routes">Routes</NavLink></li>
                <li className="nav-item"><NavLink className="nav-link" to="/admin/buses">Buses</NavLink></li>
                <li className="nav-item"><NavLink className="nav-link" to="/admin/trips">Trips</NavLink></li>
                <li className="nav-item"><NavLink className="nav-link" to="/admin/reports">Reports</NavLink></li>
              </>
            )}
          </ul>
          <div className="d-flex gap-2">
            {!isAuthScreen && user ? (
              <>
                <span className="navbar-text text-light me-2">{isAdmin ? 'Hello Admin' : `Hello ${user.name || user.email}`}</span>
                <button
                  className="btn btn-outline-light btn-sm"
                  onClick={() => { logout(); navigate('/login'); }}
                >Logout</button>
              </>
            ) : (
              <>
                <Link className="btn btn-outline-light btn-sm" to="/login">Login</Link>
                <Link className="btn btn-warning btn-sm" to="/register">Register</Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}
