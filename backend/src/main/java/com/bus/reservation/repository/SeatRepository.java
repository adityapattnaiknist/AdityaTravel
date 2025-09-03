package com.bus.reservation.repository;

import com.bus.reservation.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTripId(Long tripId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Seat> findByTripIdAndSeatNumberIn(Long tripId, Collection<String> seatNumbers);
}
