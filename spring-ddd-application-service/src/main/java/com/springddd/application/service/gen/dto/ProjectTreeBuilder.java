package com.springddd.application.service.gen.dto;

import java.util.*;

public class ProjectTreeBuilder {

    public static ProjectTreeView buildTreeFromPaths(List<String> paths) {
        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("root");

        Map<String, ProjectTreeView> nodeMap = new HashMap<>();

        for (String path : paths) {
            String[] parts = path.split("/");
            ProjectTreeView currentNode = root;

            for (String part : parts) {
                ProjectTreeView childNode = currentNode.getChildren().stream()
                        .filter(child -> child.getLabel().equals(part))
                        .findFirst()
                        .orElse(null);

                if (childNode == null) {
                    childNode = new ProjectTreeView();
                    childNode.setLabel(part);
                    childNode.setChildren(new ArrayList<>());
                    currentNode.getChildren().add(childNode);
                }

                currentNode = childNode;
            }
        }

        return root;
    }

    public static void printTree(ProjectTreeView tree, String indent) {
        if (tree.getChildren().isEmpty()) {
            System.out.println(indent + "└───" + tree.getLabel());
        } else {
            System.out.println(indent + "├───" + tree.getLabel());
            for (ProjectTreeView child : tree.getChildren()) {
                printTree(child, indent + "│   ");
            }
        }
    }
}
