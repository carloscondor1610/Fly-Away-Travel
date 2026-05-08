package com.ore.week7lab1_practica1v2.service;

import com.ore.week7lab1_practica1v2.DTO.LoginRequestDTO;
import com.ore.week7lab1_practica1v2.DTO.RegisterUserRequestDTO;
import com.ore.week7lab1_practica1v2.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class UserService {

    private Map<String, User> users = new HashMap<>();

    public String register(RegisterUserRequestDTO dto) {

        if (dto.firstName == null || dto.firstName.length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (dto.lastName == null || dto.lastName.length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (dto.email == null ||
                !dto.email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (dto.password == null ||
                !dto.password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        for (User user : users.values()) {
            if (user.getEmail().equals(dto.email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }

        User user = new User();

        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setEmail(dto.email);
        user.setPassword(dto.password);

        String id = UUID.randomUUID().toString();

        user.setId(id);

        users.put(id, user);

        return id;
    }

    public String login(LoginRequestDTO dto) {

        if (dto.email == null || dto.email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (dto.password == null || dto.password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User foundUser = null;

        for (User user : users.values()) {
            if (user.getEmail().equals(dto.email)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!foundUser.getPassword().equals(dto.password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String token = JWT.create()
                .withSubject(foundUser.getId())
                .sign(Algorithm.HMAC256("secret"));

        return token;
    }
    public Map<String, User> getAll() {
        return users;
    }

    public void deleteAll(){
        users.clear();
    }
}