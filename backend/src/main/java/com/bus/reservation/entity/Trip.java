package com.bus.reservation.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Bus bus;

    @ManyToOne(optional = false)
    private Route route;

    private OffsetDateTime departureTime;
    private OffsetDateTime arrivalTime;
    private Double fare;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Bus getBus() { return bus; }
    public void setBus(Bus bus) { this.bus = bus; }
    public Route getRoute() { return route; }
    public void setRoute(Route route) { this.route = route; }
    public OffsetDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(OffsetDateTime departureTime) { this.departureTime = departureTime; }
    public OffsetDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(OffsetDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public Double getFare() { return fare; }
    public void setFare(Double fare) { this.fare = fare; }
}
