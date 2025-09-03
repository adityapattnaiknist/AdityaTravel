import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import RequireRole from './components/RequireRole';
import Login from './pages/Login';
import Register from './pages/Register';
import SearchTrips from './pages/SearchTrips';
import SeatSelection from './pages/SeatSelection';
import Checkout from './pages/Checkout';
import Ticket from './pages/Ticket';
import AdminRoutes from './pages/AdminRoutes';
import AdminBuses from './pages/AdminBuses';
import AdminTrips from './pages/AdminTrips';
import AdminReports from './pages/AdminReports';
import MyTickets from './pages/MyTickets';
import Forbidden from './pages/Forbidden';

export default function App() {
  return (
    <>
      <Navbar />
      <div className="container py-3">
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route path="/search" element={
            <RequireRole role="CUSTOMER"><SearchTrips /></RequireRole>
          } />
          <Route path="/trips/:id/seats" element={
            <RequireRole role="CUSTOMER"><SeatSelection /></RequireRole>
          } />
          <Route path="/checkout/:bookingId" element={
            <RequireRole role="CUSTOMER"><Checkout /></RequireRole>
          } />
          <Route path="/ticket/:id" element={
            <RequireRole role="CUSTOMER"><Ticket /></RequireRole>
          } />
          <Route path="/my-tickets" element={
            <RequireRole role="CUSTOMER"><MyTickets /></RequireRole>
          } />
          <Route path="/forbidden" element={<Forbidden />} />

          <Route path="/admin/routes" element={
            <RequireRole role="ADMIN"><AdminRoutes /></RequireRole>
          } />
          <Route path="/admin/buses" element={
            <RequireRole role="ADMIN"><AdminBuses /></RequireRole>
          } />
          <Route path="/admin/trips" element={
            <RequireRole role="ADMIN"><AdminTrips /></RequireRole>
          } />
          <Route path="/admin/reports" element={
            <RequireRole role="ADMIN"><AdminReports /></RequireRole>
          } />

          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </div>
    </>
  );
}
