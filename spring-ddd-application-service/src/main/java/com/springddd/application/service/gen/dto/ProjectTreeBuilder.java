package com.springddd.application.service.gen.dto;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * <pre>
 * filePath: "xx-application-infrastructure/persistence/com/ddd/pi/infrastructure/persistence/entity/MyEntity.java"
 * content : "...Java Code..."
 *
 * ├─ xx-application-infrastructure
 * │   └─ persistence
 * │       └─ com
 * │           └─ ddd
 * │               └─ pi
 * │                   └─ infrastructure
 * │                       └─ persistence
 * │                           └─ entity
 * │                               └─ MyEntity.java  (value="...Java Code...")
 * </pre>
 *
 */
public class ProjectTreeBuilder {
    private static class Holder {
        private static final ProjectTreeBuilder INSTANCE = new ProjectTreeBuilder();
    }

    private ProjectTreeBuilder() {}

    public static ProjectTreeBuilder getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Build or update the tree from a file path and its content.
     *
     * @param root     root of the tree to build upon, or null if creating a new one
     * @param filePath relative path such as "xx-application-infrastructure/persistence/com/.../MyEntity.java"
     * @param content  file content, can be empty for directory nodes
     * @return the root of the tree
     */
    public ProjectTreeView buildTree(ProjectTreeView root, String filePath, String content) {
        String[] parts = filePath.split("/");

        // If root is null, create a new one
        if (root == null) {
            root = new ProjectTreeView();
            root.setLabel(parts[0]); // First part of the path becomes the root label
            root.setChildren(new ArrayList<>());
        }

        ProjectTreeView currentNode = root;
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            
            // Fast lookup without streams
            ProjectTreeView child = null;
            if (currentNode.getChildren() != null) {
                for (ProjectTreeView c : currentNode.getChildren()) {
                    if (c.getLabel().equals(part)) {
                        child = c;
                        break;
                    }
                }
            } else {
                currentNode.setChildren(new ArrayList<>());
            }

            if (child == null) {
                child = new ProjectTreeView();
                child.setLabel(part);
                child.setChildren(new ArrayList<>());
                currentNode.getChildren().add(child);
            }

            // If it's the last part of the path and it's a file, add the content
            if (i == parts.length - 1 && isFile(part)) {
                child.setValue(content);
            }

            currentNode = child;
        }

        return root;
    }

    private boolean isFile(String part) {
        return part.contains(".");
    }
}





























