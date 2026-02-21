package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProjectTreeView implements Serializable {

    private String label;

    private List<ProjectTreeView> children;
}
