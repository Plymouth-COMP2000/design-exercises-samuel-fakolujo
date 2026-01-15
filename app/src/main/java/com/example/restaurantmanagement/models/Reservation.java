package com.example.restaurantmanagement.models;


public class Reservation {
    public long id;
    public String username;
    public String date;      // "YYYY-MM-DD"
    public String timeSlot;  // "12:00-14:00" etc (2-hour frames)
    public int guests;
    public String specialRequest;
    public String status;    // "BOOKED" or "CANCELLED"
    public long createdAt;   // epoch millis

    public Reservation(long id, String username, String date, String timeSlot,
                       int guests, String specialRequest, String status, long createdAt) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.timeSlot = timeSlot;
        this.guests = guests;
        this.specialRequest = specialRequest;
        this.status = status;
        this.createdAt = createdAt;
    }
}
