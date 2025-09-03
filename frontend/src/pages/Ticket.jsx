import React from 'react';
import { useParams, Link } from 'react-router-dom';
import { api } from '../services/api';

export default function Ticket() {
  const { id } = useParams();
  const [ticket, setTicket] = React.useState(null);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState('');

  React.useEffect(() => {
    (async () => {
      try {
        const data = await api.tickets.get(id);
        setTicket(data);
      } catch (e) {
        setError(e?.response?.data?.message || e?.message || 'Failed to load ticket');
      } finally { setLoading(false); }
    })();
  }, [id]);

  return (
    <div className="col-md-8 mx-auto">
      <div className="d-flex justify-content-between align-items-center mb-2">
        <h3 className="m-0">Your Ticket</h3>
        <Link to="/search" className="btn btn-link">Book another</Link>
      </div>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? (
        <div>Loading...</div>
      ) : !ticket ? (
        <div className="alert alert-warning">Ticket not found</div>
      ) : (
        <div className="card">
          <div className="card-body">
            <div className="row g-3">
              <div className="col-md-4">
                <div className="text-muted">Ticket ID</div>
                <div className="fw-semibold">{ticket.id}</div>
              </div>
              <div className="col-md-4">
                <div className="text-muted">Seats</div>
                <div className="fw-semibold">{ticket.booking?.seatNumbersCsv}</div>
              </div>
              <div className="col-md-4">
                <div className="text-muted">Fare</div>
                <div className="fw-semibold">₹{ticket.booking?.totalAmount ?? '-'}</div>
              </div>
              <div className="col-md-4">
                <div className="text-muted">From</div>
                <div className="fw-semibold">{ticket.booking?.trip?.route?.source}</div>
              </div>
              <div className="col-md-4">
                <div className="text-muted">To</div>
                <div className="fw-semibold">{ticket.booking?.trip?.route?.destination}</div>
              </div>
              <div className="col-md-4">
                <div className="text-muted">Departure</div>
                <div className="fw-semibold">{(ticket.booking?.trip?.departureTime || '').toString().slice(0,16).replace('T',' ')}</div>
              </div>
            </div>
            <hr />
            <div className="d-flex align-items-center gap-3">
              <div>
                <div className="text-muted">Scan at Entry</div>
                <div><small className="text-muted">(Place scanner.png in /public/assets)</small></div>
              </div>
              <img src="/assets/scanner.png" alt="Scanner Placeholder" style={{ width: 120, height: 120, objectFit: 'contain' }} />
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
