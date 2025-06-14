package com.springddd.application.service.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserQuery implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String phone;

    private String avatar;

    private String email;

    private String sex;

    private String lockStatus;

    private Long deptId;

    private String deleteStatus;

    private String version;

    private Integer pageNum;

    private Integer pageSize;
}
