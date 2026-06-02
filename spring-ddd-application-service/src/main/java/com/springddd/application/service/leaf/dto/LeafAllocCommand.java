package com.springddd.application.service.leaf.dto;

import lombok.Data;

@Data
public class LeafAllocCommand {

    private Long id;

    private String bizTag;

    private Long maxId;

    private Integer step;

    private String description;

    private Long deptId;
}
