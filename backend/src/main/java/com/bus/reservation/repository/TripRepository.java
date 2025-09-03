package com.bus.reservation.repository;

import com.bus.reservation.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByRoute_SourceIgnoreCaseAndRoute_DestinationIgnoreCaseAndDepartureTimeBetween(
            String source,
            String destination,
            OffsetDateTime start,
            OffsetDateTime end
    );
}
