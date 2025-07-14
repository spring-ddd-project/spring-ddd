package com.springddd.application.service.menu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuPageQuery extends SysMenuQuery implements Serializable {

    @NotNull(message = "pageNum can not be null")
    private Integer pageNum;

    @NotNull(message = "pageSize can not be null")
    private Integer pageSize;
}
