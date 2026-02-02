package com.springddd.application.service.gen.interpreter;

import com.springddd.application.service.gen.dto.ProjectTreeView;
import java.util.Collections;
import java.util.List;

public class WildcardPathExpression implements PathExpression {
    @Override
    public List<ProjectTreeView> interpret(ProjectTreeView node) {
        if (node.getChildren() == null) return Collections.emptyList();
        return node.getChildren();
    }
}














































