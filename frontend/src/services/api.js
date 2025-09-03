import axios from 'axios';

// Base '/api' + explicit '/v1' paths to hit backend
export const http = axios.create({ baseURL: '/api' });

export const api = {
  auth: {
    async login(email, password) {
      const { data } = await http.post('/v1/auth/login', { email, password });
      return data;
    },
    async register(user) {
      const { data } = await http.post('/v1/auth/register', user);
      return data;
    },
  },
  routes: {
    async list() { const { data } = await http.get('/v1/routes'); return data; },
    async get(id) { const { data } = await http.get(`/v1/routes/${id}`); return data; },
    async create(payload) { const { data } = await http.post('/v1/routes', payload); return data; },
    async update(id, payload) { const { data } = await http.put(`/v1/routes/${id}`, payload); return data; },
    async remove(id) { const { data } = await http.delete(`/v1/routes/${id}`); return data; },
  },
  buses: {
    async list() { const { data } = await http.get('/v1/buses'); return data; },
    async get(id) { const { data } = await http.get(`/v1/buses/${id}`); return data; },
    async create(payload) { const { data } = await http.post('/v1/buses', payload); return data; },
    async update(id, payload) { const { data } = await http.put(`/v1/buses/${id}`, payload); return data; },
    async remove(id) { const { data } = await http.delete(`/v1/buses/${id}`); return data; },
  },
  trips: {
    async list() { const { data } = await http.get('/v1/trips'); return data; },
    async get(id) { const { data } = await http.get(`/v1/trips/${id}`); return data; },
    async create(payload) { const { data } = await http.post('/v1/trips', payload); return data; },
    async update(id, payload) { const { data } = await http.put(`/v1/trips/${id}`, payload); return data; },
    async remove(id) { const { data } = await http.delete(`/v1/trips/${id}`); return data; },
    async search({ source, destination, date }) {
      const { data } = await http.get('/v1/trips/search', { params: { source, destination, date } });
      return data;
    },
    async seats(tripId) {
      const { data } = await http.get(`/v1/trips/${tripId}/seats`);
      return data;
    },
  },
  bookings: {
    async list() { const { data } = await http.get('/v1/bookings'); return data; },
    async get(id) { const { data } = await http.get(`/v1/bookings/${id}`); return data; },
    async hold({ userId, tripId, seats }) { const { data } = await http.post('/v1/bookings/hold', { userId, tripId, seats }); return data; },
    async cancel(id) { const { data } = await http.post(`/v1/bookings/${id}/cancel`); return data; },
  },
  payments: {
    async checkout({ bookingId, paymentMethod }) {
      const { data } = await http.post('/v1/payments/checkout', { bookingId, paymentMethod });
      return data;
    },
  },
  tickets: {
    async list() { const { data } = await http.get('/v1/tickets'); return data; },
    async get(id) { const { data } = await http.get(`/v1/tickets/${id}`); return data; },
  },
  reports: {
    async sales() { const { data } = await http.get('/v1/reports/sales'); return data; },
  },
};

export default api;
