package com.springddd.application.service.auth.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUserView implements Serializable {

    private String accessToken;
}
