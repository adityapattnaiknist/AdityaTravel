package com.bus.reservation.dto;

public class BusRequest {
    private String busNumber;
    private String busType;
    private String operator;
    private String seatLayoutJson;
    private Integer totalSeats;

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

