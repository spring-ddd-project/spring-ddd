package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenAggregateCommand implements Serializable {

    private Long id;

    private Long infoId;

    private String aggregate;

    private String valueObject;

    private String domainMask;
}
