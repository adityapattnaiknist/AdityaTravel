package com.bus.reservation.service;

import com.bus.reservation.dto.TripCreateRequest;
import com.bus.reservation.entity.Bus;
import com.bus.reservation.entity.Route;
import com.bus.reservation.entity.Seat;
import com.bus.reservation.entity.Trip;
import com.bus.reservation.repository.BusRepository;
import com.bus.reservation.repository.RouteRepository;
import com.bus.reservation.repository.SeatRepository;
import com.bus.reservation.repository.TripRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final SeatRepository seatRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TripService(TripRepository tripRepository, BusRepository busRepository, RouteRepository routeRepository, SeatRepository seatRepository) {
        this.tripRepository = tripRepository;
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
        this.seatRepository = seatRepository;
    }

    public Trip save(Trip trip) { return tripRepository.save(trip); }
    public Optional<Trip> findById(Long id) { return tripRepository.findById(id); }
    public List<Trip> findAll() { return tripRepository.findAll(); }
    public void deleteById(Long id) { tripRepository.deleteById(id); }
    public Trip update(Long id, Trip updated) { updated.setId(id); return tripRepository.save(updated); }

    public List<Trip> search(String source, String destination, LocalDate date) {
        if (source == null || destination == null || date == null) {
            return tripRepository.findAll();
        }
        OffsetDateTime start = date.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = date.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC).minusNanos(1);
        return tripRepository.findByRoute_SourceIgnoreCaseAndRoute_DestinationIgnoreCaseAndDepartureTimeBetween(
                source, destination, start, end);
    }

    @Transactional
    public Trip createAndSeedSeats(TripCreateRequest req) {
        Bus bus = busRepository.findById(req.getBusId()).orElseThrow(() -> new IllegalArgumentException("Bus not found"));
        Route route = routeRepository.findById(req.getRouteId()).orElseThrow(() -> new IllegalArgumentException("Route not found"));

        Trip t = new Trip();
        t.setBus(bus);
        t.setRoute(route);
        t.setDepartureTime(req.getDepartureTime());
        t.setArrivalTime(req.getArrivalTime());
        t.setFare(req.getFare());
        Trip saved = tripRepository.save(t);

        // Only seed if no seats exist for this trip
        if (seatRepository.findByTripId(saved.getId()).isEmpty()) {
            List<Seat> toCreate = new ArrayList<>();
            String layout = bus.getSeatLayoutJson();
            if (layout != null && !layout.isBlank()) {
                try {
                    JsonNode root = objectMapper.readTree(layout);
                    JsonNode seats = root.get("seats");
                    if (seats != null && seats.isArray()) {
                        for (JsonNode node : seats) {
                            String num = node.hasNonNull("num") ? node.get("num").asText() : null;
                            if (num == null || num.isBlank()) continue;
                            String type = node.hasNonNull("type") ? node.get("type").asText() : null;
                            Seat s = new Seat();
                            s.setTrip(saved);
                            s.setSeatNumber(num);
                            s.setSeatType(type);
                            s.setBooked(false);
                            toCreate.add(s);
                        }
                    }
                } catch (Exception ignore) {
                    // Fallback to default generation below
                }
            }
            if (toCreate.isEmpty()) {
                int total = bus.getTotalSeats() != null ? bus.getTotalSeats() : 0;
                for (int i = 0; i < total; i++) {
                    String rowLabel = rowIndexToLabel(i / 4);
                    int col = (i % 4) + 1;
                    String num = rowLabel + col;
                    String type = (i % 4 == 0 || i % 4 == 3) ? "WINDOW" : "AISLE";
                    Seat s = new Seat();
                    s.setTrip(saved);
                    s.setSeatNumber(num);
                    s.setSeatType(type);
                    s.setBooked(false);
                    toCreate.add(s);
                }
            }
            if (!toCreate.isEmpty()) {
                seatRepository.saveAll(toCreate);
            }
        }

        return saved;
    }

    private static String rowIndexToLabel(int index) {
        // Excel-like labels: A..Z, AA..AZ, BA.. etc.
        StringBuilder sb = new StringBuilder();
        int i = index;
        do {
            int rem = i % 26;
            sb.append((char) ('A' + rem));
            i = (i / 26) - 1;
        } while (i >= 0);
        return sb.reverse().toString();
    }
}
