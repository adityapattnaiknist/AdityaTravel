package com.bus.reservation.repository;

import com.bus.reservation.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> { }
