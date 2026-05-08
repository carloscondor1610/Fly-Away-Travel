package com.ore.week7lab1_practica1v2.model;

public class Booking {

    private String id;
    private String bookingDate;
    private String flightId;
    private String flightNumber;
    private String customerId;
    private String customerFirstName;
    private String customerLastName;
    private String estDepartureTime;
    private String estArrivalTime;

    public Booking(
            String id,
            String bookingDate,
            String flightId,
            String flightNumber,
            String customerId,
            String customerFirstName,
            String customerLastName,
            String estDepartureTime,
            String estArrivalTime
    ) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.estDepartureTime = estDepartureTime;
        this.estArrivalTime = estArrivalTime;
    }

    public String getId() {
        return id;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public String getEstDepartureTime() {
        return estDepartureTime;
    }

    public String getEstArrivalTime() {
        return estArrivalTime;
    }
}