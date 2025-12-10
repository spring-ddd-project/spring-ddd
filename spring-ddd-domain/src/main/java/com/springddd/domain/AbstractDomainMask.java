package com.springddd.domain;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AbstractDomainMask {

    private Long deptId;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private String deleteStatus;

    private String version;
}
