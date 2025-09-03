package com.bus.reservation.controller;

import com.bus.reservation.dto.CheckoutRequest;
import com.bus.reservation.entity.Payment;
import com.bus.reservation.entity.Ticket;
import com.bus.reservation.service.BookingService;
import com.bus.reservation.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;

    public PaymentController(PaymentService paymentService, BookingService bookingService) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {
        Payment saved = paymentService.save(payment);
        return ResponseEntity.created(URI.create("/api/v1/payments/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> list() { return ResponseEntity.ok(paymentService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> get(@PathVariable Long id) { return paymentService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable Long id, @RequestBody Payment payment) { return ResponseEntity.ok(paymentService.update(id, payment)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { paymentService.deleteById(id); return ResponseEntity.noContent().build(); }

    @PostMapping("/checkout")
    public ResponseEntity<Ticket> checkout(@RequestBody CheckoutRequest req) {
        Ticket ticket = bookingService.checkout(req.getBookingId(), req.getPaymentMethod());
        return ResponseEntity.ok(ticket);
    }
}
