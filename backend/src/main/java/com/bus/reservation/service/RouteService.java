package com.bus.reservation.service;

import com.bus.reservation.entity.Route;
import com.bus.reservation.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Route save(Route route) { return routeRepository.save(route); }
    public Optional<Route> findById(Long id) { return routeRepository.findById(id); }
    public List<Route> findAll() { return routeRepository.findAll(); }
    public void deleteById(Long id) { routeRepository.deleteById(id); }
    public Route update(Long id, Route updated) { updated.setId(id); return routeRepository.save(updated); }
}

