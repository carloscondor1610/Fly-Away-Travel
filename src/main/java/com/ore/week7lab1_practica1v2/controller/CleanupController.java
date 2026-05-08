package com.ore.week7lab1_practica1v2.controller;

import com.ore.week7lab1_practica1v2.service.FlightService;
import com.ore.week7lab1_practica1v2.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cleanup")
public class CleanupController {

    private final FlightService flightService;
    private final UserService userService;

    public CleanupController(FlightService flightService,
                             UserService userService) {
        this.flightService = flightService;
        this.userService = userService;
    }

    @DeleteMapping
    public ResponseEntity<Void> cleanup(){

        flightService.deleteAll();
        userService.deleteAll();

        return ResponseEntity.ok().build();
    }
}