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
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
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
            String estDepartureTimeTo) {

        List<FlightResponseDTO> result = new ArrayList<>();

        Date fromDate = null;
        Date toDate = null;

        try {

            SimpleDateFormat format =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

            if (estDepartureTimeFrom != null &&
                    !estDepartureTimeFrom.isEmpty()) {

                fromDate = format.parse(estDepartureTimeFrom);
            }

            if (estDepartureTimeTo != null &&
                    !estDepartureTimeTo.isEmpty()) {

                toDate = format.parse(estDepartureTimeTo);
            }

        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        for (Flight flight : flights.values()) {

            boolean matches = true;

            if (flightNumber != null &&
                    !flightNumber.isEmpty()) {

                if (!flight.getFlightNumber()
                        .contains(flightNumber)) {

                    matches = false;
                }
            }

            if (airlineName != null &&
                    !airlineName.isEmpty()) {

                if (!flight.getAirlineName()
                        .toLowerCase()
                        .contains(airlineName.toLowerCase())) {

                    matches = false;
                }
            }

            if (fromDate != null) {

                if (flight.getEstDepartureTime()
                        .before(fromDate)) {

                    matches = false;
                }
            }

            if (toDate != null) {

                if (flight.getEstDepartureTime()
                        .after(toDate)) {

                    matches = false;
                }
            }

            if (matches) {
                result.add(new FlightResponseDTO(flight));
            }
        }

        result.sort(
                Comparator.comparing(
                        FlightResponseDTO::getFlightNumber
                )
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
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST
            );
        }

        if (flight.getAvailableSeats() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST
            );
        }

        if (flight.getEstDepartureTime()
                .before(new Date())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST
            );
        }

        for (Booking booking : bookings.values()) {

            Flight bookedFlight =
                    flights.get(booking.getFlightId());

            if (bookedFlight == null) {
                continue;
            }

            boolean overlap =
                    flight.getEstDepartureTime()
                            .before(
                                    bookedFlight.getEstArrivalTime()
                            )
                            &&
                            flight.getEstArrivalTime()
                                    .after(
                                            bookedFlight.getEstDepartureTime()
                                    );

            if (overlap) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        flight.setAvailableSeats(
                flight.getAvailableSeats() - 1
        );

        String bookingId =
                UUID.randomUUID().toString();

        Booking booking = new Booking(
                bookingId,
                Instant.now().toString(),
                flight.getId(),
                flight.getFlightNumber(),
                "1",
                "John",
                "Doe",
                flight.getEstDepartureTime()
                        .toInstant()
                        .toString(),
                flight.getEstArrivalTime()
                        .toInstant()
                        .toString()
        );

        bookings.put(bookingId, booking);

        try {

            FileWriter writer = new FileWriter(
                    "D:\\-cs2031-2026-1-week07-tester-main\\flight_booking_email_"
                            + bookingId +
                            ".txt"
            );

            writer.write(
                    "bookingDate: " +
                            booking.getBookingDate() + "\n" +

                            "customerFirstName: " +
                            booking.getCustomerFirstName() + "\n" +

                            "customerLastName: " +
                            booking.getCustomerLastName() + "\n" +

                            "flightNumber: " +
                            booking.getFlightNumber() + "\n" +

                            "estDepartureTime: " +
                            booking.getEstDepartureTime() + "\n" +

                            "estArrivalTime: " +
                            booking.getEstArrivalTime()
            );

            writer.close();

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        return bookingId;
    }

    public Booking getBookingById(String id){
        return bookings.get(id);
    }
    public List<String> createMany(
            List<NewFlightRequestDTO> inputs
    ) {

        List<String> ids = new ArrayList<>();

        for (NewFlightRequestDTO dto : inputs) {

            String id = create(dto);

            ids.add(id);
        }

        return ids;
    }
}