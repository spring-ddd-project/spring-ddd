package com.springddd.application.service.user.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class SysUserQuery implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String phone;

    private String avatar;

    private String email;

    private Boolean sex;

    private Boolean lockStatus;

    private Long deptId;

    private Boolean deleteStatus;

    private Integer version;
}
