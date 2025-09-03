package com.bus.reservation.entity;

import jakarta.persistence.*;

@Entity
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String busNumber;
    private String busType; // AC/Non-AC/Sleeper
    private String operator;
    @Column(columnDefinition = "TEXT")
    private String seatLayoutJson;
    private Integer totalSeats;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }
    public String getBusType() { return busType; }
    public void setBusType(String busType) { this.busType = busType; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getSeatLayoutJson() { return seatLayoutJson; }
    public void setSeatLayoutJson(String seatLayoutJson) { this.seatLayoutJson = seatLayoutJson; }
    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }
}
