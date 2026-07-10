package com.springddd.application.service.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserPostCommand implements Serializable {

    private Long id;

    private Long userId;

    private Long postId;

    private Boolean deleteStatus;
}
