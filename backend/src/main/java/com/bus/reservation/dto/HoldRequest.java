package com.bus.reservation.dto;

import java.util.List;

public class HoldRequest {
    private Long userId;
    private Long tripId;
    private List<String> seats;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public List<String> getSeats() { return seats; }
    public void setSeats(List<String> seats) { this.seats = seats; }
}

