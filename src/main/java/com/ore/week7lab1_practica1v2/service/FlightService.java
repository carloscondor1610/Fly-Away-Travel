package com.ore.week7lab1_practica1v2.service;

import com.ore.week7lab1_practica1v2.DTO.FlightBookRequestDTO;
import com.ore.week7lab1_practica1v2.DTO.FlightResponseDTO;
import com.ore.week7lab1_practica1v2.DTO.FlightSearchResponseDTO;
import com.ore.week7lab1_practica1v2.DTO.NewFlightRequestDTO;
import com.ore.week7lab1_practica1v2.model.Booking;
import com.ore.week7lab1_practica1v2.model.Flight;
import com.ore.week7lab1_practica1v2.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Service
public class FlightService {

    private Map<String, Flight> flights = new HashMap<>();
    private Map<String, Booking> bookings = new HashMap<>();

    @Autowired
    private UserService userService;

    public String create(NewFlightRequestDTO dto){

        if (dto.airlineName == null || dto.airlineName.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (dto.flightNumber == null || dto.flightNumber.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (dto.estDepartureTime == null || dto.estArrivalTime == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (dto.availableSeats == null || dto.availableSeats <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!dto.flightNumber.matches("^[A-Z]{2,3}[0-9]{3}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        for (Flight f : flights.values()) {
            if (f.getFlightNumber().equals(dto.flightNumber)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }

        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

        Date dep;
        Date arr;

        try {
            dep = format.parse(dto.estDepartureTime);
            arr = format.parse(dto.estArrivalTime);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!dep.before(arr)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Flight flight = new Flight();

        flight.setAirlineName(dto.airlineName);
        flight.setFlightNumber(dto.flightNumber);
        flight.setAvailableSeats(dto.availableSeats);
        flight.setEstDepartureTime(dep);
        flight.setEstArrivalTime(arr);

        String id = UUID.randomUUID().toString();

        flight.setId(id);

        flights.put(id, flight);

        return id;
    }

    public FlightSearchResponseDTO search(
            String flightNumber,
            String airlineName,
            String estDepartureTimeFrom,
            String estDepartureTimeTo
    ) {

        List<FlightResponseDTO> result = new ArrayList<>();

        for (Flight flight : flights.values()) {

            boolean matches = true;

            if (flightNumber != null && !flightNumber.isEmpty()) {

                if (!flight.getFlightNumber().contains(flightNumber)) {
                    matches = false;
                }
            }

            if (airlineName != null && !airlineName.isEmpty()) {

                if (!flight.getAirlineName()
                        .toLowerCase()
                        .contains(airlineName.toLowerCase())) {

                    matches = false;
                }
            }

            if (matches) {
                result.add(new FlightResponseDTO(flight));
            }
        }

        result.sort(
                Comparator.comparing(f -> f.flightNumber)
        );

        return new FlightSearchResponseDTO(result);
    }

    public Flight getById(String id){
        return flights.get(id);
    }

    public void deleteAll() {
        flights.clear();
        bookings.clear();
    }

    public String book(FlightBookRequestDTO dto) {

        Flight flight = flights.get(dto.flightId);

        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (flight.getAvailableSeats() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        flight.setAvailableSeats(
                flight.getAvailableSeats() - 1
        );

        User customer = userService
                .getAll()
                .values()
                .iterator()
                .next();

        String bookingId = UUID.randomUUID().toString();

        Booking booking = new Booking(
                bookingId,
                Instant.now().toString(),
                flight.getId(),
                flight.getFlightNumber(),
                "customer-id",
                "John",
                "Doe",
                flight.getEstDepartureTime().toInstant().toString(),
                flight.getEstArrivalTime().toInstant().toString()
        );

        bookings.put(bookingId, booking);

        return bookingId;
    }

    public Booking getBookingById(String id){
        return bookings.get(id);
    }
}