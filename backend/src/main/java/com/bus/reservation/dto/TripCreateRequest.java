package com.bus.reservation.dto;

import java.time.OffsetDateTime;

public class TripCreateRequest {
    private Long busId;
    private Long routeId;
    private OffsetDateTime departureTime;
    private OffsetDateTime arrivalTime;
    private Double fare;

    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }
    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    public OffsetDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(OffsetDateTime departureTime) { this.departureTime = departureTime; }
    public OffsetDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(OffsetDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public Double getFare() { return fare; }
    public void setFare(Double fare) { this.fare = fare; }
}

