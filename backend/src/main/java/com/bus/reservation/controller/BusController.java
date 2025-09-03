package com.bus.reservation.controller;

import com.bus.reservation.dto.BusRequest;
import com.bus.reservation.entity.Bus;
import com.bus.reservation.service.BusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/buses")
public class BusController {
    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @PostMapping
    public ResponseEntity<Bus> addBus(@RequestBody BusRequest req) {
        Bus bus = new Bus();
        bus.setBusNumber(req.getBusNumber());
        bus.setBusType(req.getBusType());
        bus.setOperator(req.getOperator());
        bus.setSeatLayoutJson(req.getSeatLayoutJson());
        bus.setTotalSeats(req.getTotalSeats());
        Bus saved = busService.save(bus);
        return ResponseEntity.created(URI.create("/api/v1/buses/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Bus>> list() { return ResponseEntity.ok(busService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Bus> get(@PathVariable Long id) { return busService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    @PutMapping("/{id}")
    public ResponseEntity<Bus> update(@PathVariable Long id, @RequestBody BusRequest req) {
        Bus bus = new Bus();
        bus.setBusNumber(req.getBusNumber());
        bus.setBusType(req.getBusType());
        bus.setOperator(req.getOperator());
        bus.setSeatLayoutJson(req.getSeatLayoutJson());
        bus.setTotalSeats(req.getTotalSeats());
        return ResponseEntity.ok(busService.update(id, bus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { busService.deleteById(id); return ResponseEntity.noContent().build(); }
}
