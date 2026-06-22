package com.springddd.application.service.leaf.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import java.io.Serializable;

@Data
@FieldNameConstants
public class LeafWorkerQuery implements Serializable {

    private Integer workerId;

    private Integer datacenterId;

    private String ip;

    private Long lastTimestamp;

    private Integer port;

    private Long id;

    private Boolean deleteStatus;

}
