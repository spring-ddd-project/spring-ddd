package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProjectTreeView implements Serializable {

    private String label;

    private String value;

    private List<ProjectTreeView> children;

    public void accept(ProjectTreeVisitor visitor) {
        visitor.visit(this);
        if (children != null) {
            children.forEach(child -> child.accept(visitor));
        }
    }
}
