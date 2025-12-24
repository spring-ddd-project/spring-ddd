package com.springddd.application.service.auth.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserInfoView implements Serializable {

    private String realName;

    private List<String> roles;
}
