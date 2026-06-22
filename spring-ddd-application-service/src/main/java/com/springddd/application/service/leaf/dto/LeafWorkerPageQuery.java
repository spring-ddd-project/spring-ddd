package com.springddd.application.service.leaf.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeafWorkerPageQuery extends LeafWorkerQuery implements Serializable {

    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;

    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
}
