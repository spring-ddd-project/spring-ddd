package com.springddd.domain.user;

public record Account(Username username, Password password, String email, String phone, Boolean lockStatus) {
}
