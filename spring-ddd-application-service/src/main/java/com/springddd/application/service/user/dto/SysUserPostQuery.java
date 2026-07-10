package com.springddd.application.service.user.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class SysUserPostQuery implements Serializable {

    private Long id;

    private Long userId;

    private Long postId;

    private Boolean deleteStatus;

    private String createBy;
}
