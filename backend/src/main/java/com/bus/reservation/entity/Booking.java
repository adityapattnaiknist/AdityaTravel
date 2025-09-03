package com.bus.reservation.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Trip trip;

    private OffsetDateTime bookingDate;
    private Double totalAmount;
    private String status;

    @Column(columnDefinition = "TEXT")
    private String seatNumbersCsv;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }
    public OffsetDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(OffsetDateTime bookingDate) { this.bookingDate = bookingDate; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSeatNumbersCsv() { return seatNumbersCsv; }
    public void setSeatNumbersCsv(String seatNumbersCsv) { this.seatNumbersCsv = seatNumbersCsv; }
}
