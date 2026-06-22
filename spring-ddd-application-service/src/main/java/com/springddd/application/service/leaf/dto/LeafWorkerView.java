package com.springddd.application.service.leaf.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class LeafWorkerView implements Serializable {

    private Integer workerId;

    private Integer datacenterId;

    private String ip;

    private Long lastTimestamp;

    private Integer port;

    private Long id;

}
