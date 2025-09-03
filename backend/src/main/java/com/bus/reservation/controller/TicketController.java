package com.bus.reservation.controller;

import com.bus.reservation.entity.Ticket;
import com.bus.reservation.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) { this.ticketService = ticketService; }

    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody Ticket ticket) {
        Ticket saved = ticketService.save(ticket);
        return ResponseEntity.created(URI.create("/api/v1/tickets/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> list() { return ResponseEntity.ok(ticketService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> get(@PathVariable Long id) { return ticketService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> update(@PathVariable Long id, @RequestBody Ticket ticket) { return ResponseEntity.ok(ticketService.update(id, ticket)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { ticketService.deleteById(id); return ResponseEntity.noContent().build(); }
}
