# API Spec (Base URL: /api/v1)

## Notes
- Dev profile has no JWT; login returns a user object only. Protect endpoints by role in production.
- Trip creation auto‑seeds seats based on bus layout or capacity.
- Holds use pessimistic locking; seat limit is max 5 per user per (route, date).

## Authentication
- POST `/auth/register` — Register a CUSTOMER
  - Body: `{ name, email, phone, password }`
  - 201 → `UserResponse { id, name, email, phone, role }`
- POST `/auth/login` — Login (DB‑check only)
  - Body: `{ email, password }`
  - 200 → `LoginResponse { user, message }`

## Routes (Admin)
- GET `/routes` — List
- POST `/routes` — Create `{ source, destination, distance, duration(min) }`
- GET `/routes/{id}` — Get
- PUT `/routes/{id}` — Update
- DELETE `/routes/{id}` — Delete

## Buses (Admin)
- GET `/buses` — List
- POST `/buses` — Create `{ busNumber, busType, operator, totalSeats, seatLayoutJson? }`
- GET `/buses/{id}` — Get
- PUT `/buses/{id}` — Update
- DELETE `/buses/{id}` — Delete

## Trips
- GET `/trips` — List
- POST `/trips` — Create `TripCreateRequest { busId, routeId, departureTime, arrivalTime, fare }` (auto‑seats)
- GET `/trips/{id}` — Get
- PUT `/trips/{id}` — Update
- DELETE `/trips/{id}` — Delete
- GET `/trips/search?source=&destination=&date=YYYY-MM-DD` — Search
- GET `/trips/{id}/seats` — Seat availability (array of seats)

## Bookings
- GET `/bookings` — List
- GET `/bookings/{id}` — Get
- POST `/bookings` — Create (generic)
- PUT `/bookings/{id}` — Update
- DELETE `/bookings/{id}` — Delete
- POST `/bookings/hold` — Hold seats (transactional)
  - Body: `HoldRequest { userId, tripId, seats: ["A1","A2"] }`
  - 200 → `HoldResponse { bookingId, status, expiresAt? }`
  - 409 → conflicts or seat limit exceeded
- POST `/bookings/{id}/cancel` — Cancel and release seats

## Payments
- GET `/payments` — List
- GET `/payments/{id}` — Get
- POST `/payments` — Create
- PUT `/payments/{id}` — Update
- DELETE `/payments/{id}` — Delete
- POST `/payments/checkout` — Confirm + pay (mock)
  - Body: `CheckoutRequest { bookingId, paymentMethod }`
  - 200 → `Ticket`

## Tickets
- GET `/tickets` — List
- GET `/tickets/{id}` — Get
- POST `/tickets` — Create (normally via checkout)
- PUT `/tickets/{id}` — Update
- DELETE `/tickets/{id}` — Delete

## Reports (Admin)
- GET `/reports/sales` — Sales summary `{ totalSales, successfulPayments, allPayments }`

