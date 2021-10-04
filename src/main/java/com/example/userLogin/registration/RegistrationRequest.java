package com.example.userLogin.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest { //Quando faço o resgistro, coleto algumas informações
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
}
