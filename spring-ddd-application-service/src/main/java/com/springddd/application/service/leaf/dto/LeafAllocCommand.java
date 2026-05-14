package com.springddd.application.service.leaf.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LeafAllocCommand implements Serializable {

    private Long id;

    private String bizTag;

    private Long maxId;

    private Integer step;

    private String description;

    private Boolean deleteStatus;
}
