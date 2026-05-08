package com.ore.week7lab1_practica1v2.controller;

import com.ore.week7lab1_practica1v2.DTO.LoginRequestDTO;
import com.ore.week7lab1_practica1v2.DTO.LoginResponseDTO;
import com.ore.week7lab1_practica1v2.DTO.NewIdDTO;
import com.ore.week7lab1_practica1v2.DTO.RegisterUserRequestDTO;
import com.ore.week7lab1_practica1v2.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<NewIdDTO> register(@RequestBody RegisterUserRequestDTO dto) {

        String id = userService.register(dto);

        return ResponseEntity.status(201).body(new NewIdDTO(id));
    }

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