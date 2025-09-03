import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { api } from '../services/api';

export default function MyTickets() {
  const { user } = useAuth();
  const [items, setItems] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState('');

  React.useEffect(() => {
    (async () => {
      setLoading(true); setError('');
      try {
        const list = await api.tickets.list();
        const mine = Array.isArray(list) ? list.filter(t => t?.booking?.user?.id === user?.id) : [];
        setItems(mine);
      } catch (e) { setError(e?.response?.data?.message || e?.message || 'Failed to load tickets'); }
      finally { setLoading(false); }
    })();
  }, [user?.id]);

  return (
    <div>
      <h3>My Tickets</h3>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? 'Loading...' : (
        items.length === 0 ? (
          <div className="alert alert-info">No tickets found.</div>
        ) : (
          <div className="table-responsive">
            <table className="table table-hover align-middle">
              <thead className="table-light">
                <tr>
                  <th>Ticket</th>
                  <th>Route</th>
                  <th>Departure</th>
                  <th>Seats</th>
                  <th>Amount</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {items.map(t => (
                  <tr key={t.id}>
                    <td>{t.ticketNo || t.id}</td>
                    <td>{t.booking?.trip?.route?.source} → {t.booking?.trip?.route?.destination}</td>
                    <td>{(t.booking?.trip?.departureTime || '').toString().slice(0,16).replace('T',' ')}</td>
                    <td>{t.booking?.seatNumbersCsv}</td>
                    <td>{t.booking?.totalAmount != null ? `₹${t.booking?.totalAmount}` : '-'}</td>
                    <td className="text-end"><Link className="btn btn-sm btn-outline-primary" to={`/ticket/${t.id}`}>View</Link></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )
      )}
    </div>
  );
}

