package com.springddd.application.service.gen.interpreter;

import com.springddd.application.service.gen.dto.ProjectTreeView;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExactPathExpression implements PathExpression {
    private final String label;

    public ExactPathExpression(String label) {
        this.label = label;
    }

    @Override
    public List<ProjectTreeView> interpret(ProjectTreeView node) {
        if (node.getChildren() == null) return Collections.emptyList();
        return node.getChildren().stream()
                .filter(child -> child.getLabel().equals(label))
                .collect(Collectors.toList());
    }
}


















