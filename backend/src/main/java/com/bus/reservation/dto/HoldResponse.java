package com.bus.reservation.dto;

import java.time.OffsetDateTime;

public class HoldResponse {
    private Long bookingId;
    private String status;
    private OffsetDateTime expiresAt;

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public OffsetDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(OffsetDateTime expiresAt) { this.expiresAt = expiresAt; }
}

