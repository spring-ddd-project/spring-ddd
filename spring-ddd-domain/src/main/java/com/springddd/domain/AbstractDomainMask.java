package com.springddd.domain;


import java.time.LocalDateTime;

public abstract class AbstractDomainMask {

    private Long deptId;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private String deleteStatus;

    private String version;
}
