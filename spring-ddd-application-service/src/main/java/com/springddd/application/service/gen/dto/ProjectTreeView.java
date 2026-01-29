package com.springddd.application.service.gen.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

@Data
public class ProjectTreeView implements Serializable, Iterable<ProjectTreeView> {

    private String label;

    private String value;

    private List<ProjectTreeView> children;

    public void accept(ProjectTreeVisitor visitor) {
        visitor.visit(this);
        if (children != null) {
            children.forEach(child -> child.accept(visitor));
        }
    }

    @Override
    public Iterator<ProjectTreeView> iterator() {
        return new ProjectTreeIterator(this);
    }

    private static class ProjectTreeIterator implements Iterator<ProjectTreeView> {
        private final Stack<ProjectTreeView> stack = new Stack<>();

        public ProjectTreeIterator(ProjectTreeView root) {
            stack.push(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public ProjectTreeView next() {
            ProjectTreeView node = stack.pop();
            if (node.getChildren() != null) {
                for (int i = node.getChildren().size() - 1; i >= 0; i--) {
                    stack.push(node.getChildren().get(i));
                }
            }
            return node;
        }
    }
}
































