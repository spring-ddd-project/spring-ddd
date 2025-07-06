package com.springddd.domain.user;

import lombok.Data;

@Data
public class Account {

    private Username username;

    private Password password;

    private String email;

    private String phone;

    private Boolean lockStatus;

}
