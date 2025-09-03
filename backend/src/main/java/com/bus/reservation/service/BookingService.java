package com.bus.reservation.service;

import com.bus.reservation.entity.*;
import com.bus.reservation.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;

    public BookingService(
            BookingRepository bookingRepository,
            SeatRepository seatRepository,
            UserRepository userRepository,
            TripRepository tripRepository,
            PaymentRepository paymentRepository,
            TicketRepository ticketRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
    }

    public Booking save(Booking booking) { return bookingRepository.save(booking); }
    public Optional<Booking> findById(Long id) { return bookingRepository.findById(id); }
    public List<Booking> findAll() { return bookingRepository.findAll(); }
    public void deleteById(Long id) { bookingRepository.deleteById(id); }
    public Booking update(Long id, Booking updated) { updated.setId(id); return bookingRepository.save(updated); }

    @Transactional
    public Booking holdSeats(Long userId, Long tripId, List<String> seatNumbers) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("Trip not found"));

        // Enforce max 5 seats per user per (source, destination, date)
        if (trip.getRoute() != null && trip.getDepartureTime() != null) {
            String source = trip.getRoute().getSource();
            String destination = trip.getRoute().getDestination();
            // Start/end of departure date in UTC
            java.time.LocalDate d = trip.getDepartureTime().toLocalDate();
            java.time.OffsetDateTime start = d.atStartOfDay().atOffset(java.time.ZoneOffset.UTC);
            java.time.OffsetDateTime end = d.plusDays(1).atStartOfDay().atOffset(java.time.ZoneOffset.UTC).minusNanos(1);
            List<Booking> existing = bookingRepository
                    .findByUserIdAndTrip_Route_SourceIgnoreCaseAndTrip_Route_DestinationIgnoreCaseAndTrip_DepartureTimeBetween(
                            userId, source, destination, start, end);
            int already = existing.stream()
                    .filter(b -> b.getStatus() != null && !"CANCELLED".equalsIgnoreCase(b.getStatus()))
                    .mapToInt(b -> {
                        String csv = b.getSeatNumbersCsv();
                        if (csv == null || csv.isBlank()) return 0;
                        return (int) java.util.Arrays.stream(csv.split(",")).filter(s -> !s.isBlank()).count();
                    })
                    .sum();
            int requested = seatNumbers == null ? 0 : seatNumbers.size();
            if (already + requested > 5) {
                throw new IllegalStateException("Seat limit exceeded: max 5 per user for the same route and date");
            }
        }

        List<Seat> seats = seatRepository.findByTripIdAndSeatNumberIn(tripId, seatNumbers);
        // Check conflicts
        boolean anyBooked = seats.stream().anyMatch(Seat::isBooked);
        if (anyBooked || seats.size() != seatNumbers.size()) {
            throw new IllegalStateException("One or more seats are unavailable");
        }
        // Lock + mark booked
        seats.forEach(s -> s.setBooked(true));
        seatRepository.saveAll(seats);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrip(trip);
        booking.setBookingDate(OffsetDateTime.now());
        booking.setStatus("HOLD");
        double total = (trip.getFare() == null ? 0.0 : trip.getFare()) * seatNumbers.size();
        booking.setTotalAmount(total);
        booking.setSeatNumbersCsv(String.join(",", seatNumbers));

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getSeatNumbersCsv() != null && booking.getTrip() != null) {
            List<String> seatNumbers = Arrays.stream(booking.getSeatNumbersCsv().split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            if (!seatNumbers.isEmpty()) {
                List<Seat> seats = seatRepository.findByTripIdAndSeatNumberIn(booking.getTrip().getId(), seatNumbers);
                seats.forEach(s -> s.setBooked(false));
                seatRepository.saveAll(seats);
            }
        }
        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }

    @Transactional
    public Ticket checkout(Long bookingId, String paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Create payment record (mock success)
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setStatus("SUCCESS");
        payment.setGatewayRef(paymentMethod + "-" + UUID.randomUUID());
        paymentRepository.save(payment);

        // Confirm booking
        booking.setStatus("CONFIRMED");
        bookingRepository.save(booking);

        // Issue ticket
        Ticket ticket = new Ticket();
        ticket.setBooking(booking);
        ticket.setTicketNo("TKT-" + System.currentTimeMillis());
        ticket.setQrCode("QR:" + booking.getId());
        return ticketRepository.save(ticket);
    }
}
