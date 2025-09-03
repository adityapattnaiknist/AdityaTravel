package com.bus.reservation.service;

import com.bus.reservation.entity.Bus;
import com.bus.reservation.repository.BusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusService {
    private final BusRepository busRepository;

    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public Bus save(Bus bus) { return busRepository.save(bus); }
    public Optional<Bus> findById(Long id) { return busRepository.findById(id); }
    public List<Bus> findAll() { return busRepository.findAll(); }
    public void deleteById(Long id) { busRepository.deleteById(id); }
    public Bus update(Long id, Bus updated) { updated.setId(id); return busRepository.save(updated); }
}

