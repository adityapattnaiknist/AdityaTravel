import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import SeatMap from '../components/SeatMap';
import { api } from '../services/api';

export default function SeatSelection() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [seats, setSeats] = React.useState([]);
  const [error, setError] = React.useState('');
  const [loading, setLoading] = React.useState(true);
  const [selected, setSelected] = React.useState([]);
  const [holding, setHolding] = React.useState(false);

  const load = React.useCallback(async () => {
    setError(''); setLoading(true);
    try {
      const list = await api.trips.seats(id);
      setSeats(Array.isArray(list) ? list : []);
    } catch (e) {
      setError(e?.response?.data?.message || e?.message || 'Failed to load seats');
    } finally { setLoading(false); }
  }, [id]);

  React.useEffect(() => { load(); }, [load]);

  const toggle = (s) => {
    setSelected((prev) =>
      prev.includes(s.seatNumber)
        ? prev.filter((x) => x !== s.seatNumber)
        : [...prev, s.seatNumber]
    );
  };

  const holdSeats = async () => {
    if (selected.length === 0) return alert('Select at least one seat');
    setHolding(true);
    try {
      const res = await api.bookings.hold({ userId: user?.id, tripId: Number(id), seats: selected });
      const bookingId = res?.bookingId || res?.id;
      if (!bookingId) throw new Error('Invalid hold response');
      navigate(`/checkout/${bookingId}`, { replace: true });
    } catch (e) {
      setError(e?.response?.data?.message || e?.message || 'Hold failed, some seats unavailable.');
      await load();
    } finally { setHolding(false); }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-end mb-2">
        <h3 className="m-0">Select Seats</h3>
        <div className="text-muted">Selected: {selected.join(', ') || 'None'}</div>
      </div>
      {error && <div className="alert alert-warning">{error}</div>}
      {loading ? (
        <div>Loading seats...</div>
      ) : (
        <>
          <SeatMap seats={seats} selectedSeatNumbers={selected} onToggle={toggle} />
          <div className="d-flex justify-content-end mt-3">
            <button className="btn btn-primary" onClick={holdSeats} disabled={holding}>{holding? 'Holding...' : 'Hold & Checkout'}</button>
          </div>
        </>
      )}
    </div>
  );
}
