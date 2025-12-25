package com.springddd.application.service.gen.interpreter;

import com.springddd.application.service.gen.dto.ProjectTreeView;
import java.util.List;

public interface PathExpression {
    List<ProjectTreeView> interpret(ProjectTreeView node);
}

















