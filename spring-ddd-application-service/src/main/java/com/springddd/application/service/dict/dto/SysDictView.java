package com.springddd.application.service.dict.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysDictView implements Serializable {

    private Long id;

    private String dictName;

    private String dictCode;

    private Integer sortOrder;

    private Boolean dictStatus;

    private Boolean deleteStatus;
}
