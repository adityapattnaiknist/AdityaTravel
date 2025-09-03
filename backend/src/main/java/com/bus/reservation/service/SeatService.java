package com.bus.reservation.service;

import com.bus.reservation.entity.Seat;
import com.bus.reservation.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat save(Seat seat) { return seatRepository.save(seat); }
    public Optional<Seat> findById(Long id) { return seatRepository.findById(id); }
    public List<Seat> findAll() { return seatRepository.findAll(); }
    public void deleteById(Long id) { seatRepository.deleteById(id); }
    public Seat update(Long id, Seat updated) { updated.setId(id); return seatRepository.save(updated); }

    public List<Seat> findByTripId(Long tripId) { return seatRepository.findByTripId(tripId); }
}
