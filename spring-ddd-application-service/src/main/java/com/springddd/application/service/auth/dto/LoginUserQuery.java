package com.springddd.application.service.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUserQuery implements Serializable {

    @NotNull(message = "username can not be null")
    private String username;

    @NotNull(message = "password can not be null")
    private String password;
}
