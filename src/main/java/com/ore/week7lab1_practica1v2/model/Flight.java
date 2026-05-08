package com.ore.week7lab1_practica1v2.model;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Flight {
    private String id;
    private String airlineName;
    private String flightNumber;
    private Date estDepartureTime;
    private Date estArrivalTime;
    private Integer availableSeats;
}


