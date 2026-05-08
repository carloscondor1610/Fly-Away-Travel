package com.ore.week7lab1_practica1v2.controller;

import com.ore.week7lab1_practica1v2.DTO.LoginRequestDTO;
import com.ore.week7lab1_practica1v2.DTO.LoginResponseDTO;
import com.ore.week7lab1_practica1v2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO dto
    ) {

        String token = userService.login(dto);

        return ResponseEntity.ok(
                new LoginResponseDTO(token)
        );
    }
}