package com.springddd.application.service.leaf.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class LeafAllocView implements Serializable {
    
    private Integer step;

    private String bizTag;

    private Long maxId;

    private String description;

    private Long id;

}
