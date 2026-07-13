package com.springddd.application.service.post.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysPostView implements Serializable {

    private Long id;

    private String postCode;

    private String postName;

    private Long parentId;

    private Integer sortOrder;

    private Boolean postStatus;

    private Boolean deleteStatus;

    private List<SysPostView> children;
}
