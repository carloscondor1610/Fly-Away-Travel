package com.ore.week7lab1_practica1v2.controller;

import com.ore.week7lab1_practica1v2.DTO.FlightBookRequestDTO;
import com.ore.week7lab1_practica1v2.DTO.FlightSearchResponseDTO;
import com.ore.week7lab1_practica1v2.DTO.NewIdDTO;
import com.ore.week7lab1_practica1v2.model.Booking;
import com.ore.week7lab1_practica1v2.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ore.week7lab1_practica1v2.service.FlightService;
import com.ore.week7lab1_practica1v2.DTO.NewFlightRequestDTO;
import com.ore.week7lab1_practica1v2.DTO.CreateManyFlightsRequestDTO;
import java.util.List;


@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private final FlightService flightService;
    public FlightController(FlightService flightService){
        this.flightService = flightService;
    }

    @PostMapping("/create")
    public ResponseEntity<NewIdDTO> create(@RequestBody NewFlightRequestDTO dto) {

        String id = flightService.create(dto);


        return ResponseEntity.status(201).body(new NewIdDTO(id));
    }
    @GetMapping("/search")
    public ResponseEntity<FlightSearchResponseDTO> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false) String estDepartureTimeFrom,
            @RequestParam(required = false) String estDepartureTimeTo
    ) {

        FlightSearchResponseDTO response = flightService.search(
                flightNumber,
                airlineName,
                estDepartureTimeFrom,
                estDepartureTimeTo
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getById(@PathVariable String id){

        Flight flight = flightService.getById(id);

        if(flight == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(flight);
    }

    @PostMapping("/book")
    public ResponseEntity<NewIdDTO> book(
            @RequestBody FlightBookRequestDTO requestDTO
    ) {

        String id = flightService.book(requestDTO);

        return ResponseEntity.ok(
                new NewIdDTO(id)
        );
    }
    @GetMapping("/book/{id}")
    public ResponseEntity<Booking> getBookingById(
            @PathVariable String id
    ) {

        Booking booking = flightService.getBookingById(id);

        if (booking == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(booking);
    }
    @PostMapping("/create-many")
    public ResponseEntity<List<String>> createMany(
            @RequestBody CreateManyFlightsRequestDTO dto
    ) {

        List<String> ids =
                flightService.createMany(dto.inputs);

        return ResponseEntity
                .status(201)
                .body(ids);
    }
}
