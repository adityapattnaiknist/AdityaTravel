package com.bus.reservation.controller;

import com.bus.reservation.dto.HoldRequest;
import com.bus.reservation.dto.HoldResponse;
import com.bus.reservation.entity.Booking;
import com.bus.reservation.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) { this.bookingService = bookingService; }

    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        Booking saved = bookingService.save(booking);
        return ResponseEntity.created(URI.create("/api/v1/bookings/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> list() { return ResponseEntity.ok(bookingService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> get(@PathVariable Long id) { return bookingService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> update(@PathVariable Long id, @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.update(id, booking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { bookingService.deleteById(id); return ResponseEntity.noContent().build(); }

    @PostMapping("/hold")
    public ResponseEntity<?> holdSeats(@RequestBody HoldRequest req) {
        try {
            Booking b = bookingService.holdSeats(req.getUserId(), req.getTripId(), req.getSeats());
            HoldResponse res = new HoldResponse();
            res.setBookingId(b.getId());
            res.setStatus(b.getStatus());
            res.setExpiresAt(null); // Could be set if hold expiration is implemented
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(java.util.Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
