package com.ore.week7lab1_practica1v2.DTO;

import java.util.List;

public class FlightSearchResponseDTO {

    public List<FlightResponseDTO> items;

    public FlightSearchResponseDTO(List<FlightResponseDTO> items) {
        this.items = items;
    }
}