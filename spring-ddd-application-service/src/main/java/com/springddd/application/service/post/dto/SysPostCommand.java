package com.springddd.application.service.post.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysPostCommand implements Serializable {

    private Long id;

    private String postCode;

    private String postName;

    private Long parentId;

    private Integer sortOrder;

    private Boolean postStatus;

    private Boolean deleteStatus;
}
