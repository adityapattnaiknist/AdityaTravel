package com.bus.reservation.controller;

import com.bus.reservation.dto.RouteRequest;
import com.bus.reservation.entity.Route;
import com.bus.reservation.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) { this.routeService = routeService; }

    @PostMapping
    public ResponseEntity<Route> addRoute(@RequestBody RouteRequest req) {
        Route r = new Route();
        r.setSource(req.getSource());
        r.setDestination(req.getDestination());
        r.setDistance(req.getDistance());
        r.setDuration(req.getDuration());
        Route saved = routeService.save(r);
        return ResponseEntity.created(URI.create("/api/v1/routes/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Route>> list() { return ResponseEntity.ok(routeService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Route> get(@PathVariable Long id) { return routeService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    @PutMapping("/{id}")
    public ResponseEntity<Route> update(@PathVariable Long id, @RequestBody RouteRequest req) {
        Route r = new Route();
        r.setSource(req.getSource());
        r.setDestination(req.getDestination());
        r.setDistance(req.getDistance());
        r.setDuration(req.getDuration());
        return ResponseEntity.ok(routeService.update(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { routeService.deleteById(id); return ResponseEntity.noContent().build(); }
}
