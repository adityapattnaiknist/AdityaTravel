package com.bus.reservation.controller;

import com.bus.reservation.entity.Seat;
import com.bus.reservation.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/seats")
public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) { this.seatService = seatService; }

    @PostMapping
    public ResponseEntity<Seat> create(@RequestBody Seat seat) {
        Seat saved = seatService.save(seat);
        return ResponseEntity.created(URI.create("/api/v1/seats/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Seat>> list() { return ResponseEntity.ok(seatService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> get(@PathVariable Long id) { return seatService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    @PutMapping("/{id}")
    public ResponseEntity<Seat> update(@PathVariable Long id, @RequestBody Seat seat) { return ResponseEntity.ok(seatService.update(id, seat)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { seatService.deleteById(id); return ResponseEntity.noContent().build(); }
}

