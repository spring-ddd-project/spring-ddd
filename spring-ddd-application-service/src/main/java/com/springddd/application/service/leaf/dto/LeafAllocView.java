package com.springddd.application.service.leaf.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LeafAllocView {

    private Long id;

    private String bizTag;

    private Long maxId;

    private Integer step;

    private String description;

    private LocalDateTime updateTime;

    private Integer version;

    private Boolean deleteStatus;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private Long deptId;
}
