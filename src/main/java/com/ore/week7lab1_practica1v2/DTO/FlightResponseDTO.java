package com.ore.week7lab1_practica1v2.DTO;

import com.ore.week7lab1_practica1v2.model.Flight;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;


@Getter
@Setter
public class FlightResponseDTO {

    public String id;
    public String flightNumber;
    public String airlineName;
    public Integer availableSeats;
    public Date estDepartureTime;
    public Date estArrivalTime;

    public FlightResponseDTO(Flight flight) {
        this.id = flight.getId();
        this.flightNumber = flight.getFlightNumber();
        this.airlineName = flight.getAirlineName();
        this.availableSeats = flight.getAvailableSeats();
        this.estDepartureTime = flight.getEstDepartureTime();
        this.estArrivalTime = flight.getEstArrivalTime();
    }
}