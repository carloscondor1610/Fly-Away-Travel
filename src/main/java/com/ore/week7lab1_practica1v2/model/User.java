package com.ore.week7lab1_practica1v2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;


}
