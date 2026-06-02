package com.springddd.application.service.dict.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictPageQuery extends SysDictQuery implements Serializable {

    @NotNull(message = "pageNum can not be null")
    private Integer pageNum;

    @NotNull(message = "pageSize can not be null")
    private Integer pageSize;
}
