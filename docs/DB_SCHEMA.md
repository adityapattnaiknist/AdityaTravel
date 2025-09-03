# Database Design (MySQL, 3NF)

## Entities
- User: id, name, email, phone, password, role
- Bus: id, busNumber, busType, operator, seatLayoutJson, totalSeats
- Route: id, source, destination, distance, duration
- Trip: id, busId, routeId, departureTime, arrivalTime, fare
- Seat: id, tripId, seatNumber, seatType, isBooked
- Booking: id, userId, tripId, bookingDate, totalAmount, status
- Payment: id, bookingId, status, gatewayRef, amount
- Ticket: id, bookingId, ticketNo, pdfPath, qrCode

## Relationships
- User ↔ Booking (1–M)
- Bus ↔ Trip (1–M)
- Route ↔ Trip (1–M)
- Trip ↔ Booking (1–M)
- Booking ↔ Payment (1–1)
- Booking ↔ Ticket (1–1)
