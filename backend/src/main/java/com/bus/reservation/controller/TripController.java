package com.bus.reservation.controller;

import com.bus.reservation.dto.TripCreateRequest;
import com.bus.reservation.entity.Bus;
import com.bus.reservation.entity.Route;
import com.bus.reservation.entity.Seat;
import com.bus.reservation.entity.Trip;
import com.bus.reservation.service.BusService;
import com.bus.reservation.service.RouteService;
import com.bus.reservation.service.SeatService;
import com.bus.reservation.service.TripService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {
    private final TripService tripService;
    private final BusService busService;
    private final RouteService routeService;
    private final SeatService seatService;

    public TripController(TripService tripService, BusService busService, RouteService routeService, SeatService seatService) {
        this.tripService = tripService;
        this.busService = busService;
        this.routeService = routeService;
        this.seatService = seatService;
    }

    @PostMapping
    public ResponseEntity<Trip> create(@RequestBody TripCreateRequest req) {
        Trip saved = tripService.createAndSeedSeats(req);
        return ResponseEntity.created(URI.create("/api/v1/trips/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Trip>> list() { return ResponseEntity.ok(tripService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> get(@PathVariable Long id) { return tripService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> update(@PathVariable Long id, @RequestBody TripCreateRequest req) {
        Bus bus = busService.findById(req.getBusId()).orElse(null);
        Route route = routeService.findById(req.getRouteId()).orElse(null);
        if (bus == null || route == null) return ResponseEntity.badRequest().build();
        Trip t = new Trip();
        t.setBus(bus);
        t.setRoute(route);
        t.setDepartureTime(req.getDepartureTime());
        t.setArrivalTime(req.getArrivalTime());
        t.setFare(req.getFare());
        return ResponseEntity.ok(tripService.update(id, t));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { tripService.deleteById(id); return ResponseEntity.noContent().build(); }

    @GetMapping("/search")
    public ResponseEntity<List<Trip>> searchTrips(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(tripService.search(source, destination, date));
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity<List<Seat>> seats(@PathVariable Long id) {
        return ResponseEntity.ok(seatService.findByTripId(id));
    }
}
