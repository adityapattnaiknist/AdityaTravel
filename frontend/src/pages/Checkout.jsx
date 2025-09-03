import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../services/api';

export default function Checkout() {
  const { bookingId } = useParams();
  const navigate = useNavigate();
  const [card, setCard] = React.useState({ number: '', exp: '', cvv: '' });
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState('');
  const [booking, setBooking] = React.useState(null);
  const [loadingBooking, setLoadingBooking] = React.useState(true);

  React.useEffect(() => {
    (async () => {
      setLoadingBooking(true);
      try { setBooking(await api.bookings.get(Number(bookingId))); } catch (e) { /* ignore in UI */ }
      finally { setLoadingBooking(false); }
    })();
  }, [bookingId]);

  const onPay = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const payRes = await api.payments.checkout({ bookingId: Number(bookingId), paymentMethod: 'MOCK_CARD' });
      const ticketId = payRes?.id;
      if (!ticketId) throw new Error('Payment did not return ticket id');
      navigate(`/ticket/${ticketId}`, { replace: true });
    } catch (err) {
      setError(err?.response?.data?.message || err?.message || 'Checkout failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="col-md-8 mx-auto">
      <h3>Checkout</h3>
      {!bookingId && <div className="alert alert-warning">No booking in progress. Go back and choose seats.</div>}
      {booking && (
        <div className="card card-body mb-3">
          <div className="row g-3">
            <div className="col-md-4">
              <div className="text-muted">Route</div>
              <div className="fw-semibold">{booking.trip?.route?.source} → {booking.trip?.route?.destination}</div>
            </div>
            <div className="col-md-4">
              <div className="text-muted">Departure</div>
              <div className="fw-semibold">{(booking.trip?.departureTime || '').toString().slice(0,16).replace('T',' ')}</div>
            </div>
            <div className="col-md-2">
              <div className="text-muted">Seats</div>
              <div className="fw-semibold">{booking.seatNumbersCsv}</div>
            </div>
            <div className="col-md-2">
              <div className="text-muted">Amount</div>
              <div className="fw-semibold">₹{booking.totalAmount ?? '-'}</div>
            </div>
          </div>
        </div>
      )}
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={onPay} className="row g-3">
        <div className="col-md-6">
          <label className="form-label">Card Number</label>
          <input className="form-control" value={card.number} onChange={(e)=>setCard({...card, number: e.target.value})} required />
        </div>
        <div className="col-md-3">
          <label className="form-label">Expiry</label>
          <input placeholder="MM/YY" className="form-control" value={card.exp} onChange={(e)=>setCard({...card, exp: e.target.value})} required />
        </div>
        <div className="col-md-3">
          <label className="form-label">CVV</label>
          <input className="form-control" value={card.cvv} onChange={(e)=>setCard({...card, cvv: e.target.value})} required />
        </div>
        <div className="col-12 d-flex justify-content-end">
          <button className="btn btn-success" disabled={loading || !bookingId || loadingBooking}>{loading? 'Processing...' : 'Pay & Get Ticket'}</button>
        </div>
      </form>
    </div>
  );
}
