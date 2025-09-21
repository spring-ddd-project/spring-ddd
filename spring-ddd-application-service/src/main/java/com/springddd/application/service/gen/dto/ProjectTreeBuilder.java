package com.springddd.application.service.gen.dto;

import java.util.ArrayList;

public class ProjectTreeBuilder {

    public ProjectTreeView buildTree(String filePath, String content) {
        String[] parts = filePath.split("/");

        return buildTreeRecursive(parts, 0, content);
    }

    private ProjectTreeView buildTreeRecursive(String[] parts, int index, String content) {
        if (index >= parts.length) {
            return null;
        }

        String currentPart = parts[index];

        ProjectTreeView currentNode = new ProjectTreeView();
        currentNode.setLabel(currentPart);
        currentNode.setChildren(new ArrayList<>());

        if (index == parts.length - 1 && isFile(currentPart)) {
            currentNode.setValue(content);
        }

        ProjectTreeView childNode = buildTreeRecursive(parts, index + 1, content);
        if (childNode != null) {
            currentNode.getChildren().add(childNode);
        }

        return currentNode;
    }

    private boolean isFile(String part) {
        return part.contains(".");
    }
}