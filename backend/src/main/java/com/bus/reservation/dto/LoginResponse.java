package com.bus.reservation.dto;

public class LoginResponse {
    private UserResponse user;
    private String message;

    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

