package com.bus.reservation.repository;

import com.bus.reservation.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdAndTrip_Route_SourceIgnoreCaseAndTrip_Route_DestinationIgnoreCaseAndTrip_DepartureTimeBetween(
            Long userId,
            String source,
            String destination,
            OffsetDateTime start,
            OffsetDateTime end
    );
}
