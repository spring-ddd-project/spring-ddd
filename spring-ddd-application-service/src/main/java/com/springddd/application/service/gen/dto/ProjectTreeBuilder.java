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

    /**
     * Build the whole tree from a file path and its content.
     *
     * @param filePath relative path such as "xx-application-infrastructure/persistence/com/.../MyEntity.java"
     * @param content  file content, can be empty for directory nodes
     * @return root of the generated tree (might contain multiple top‑level children)
     */
    public ProjectTreeView buildTree(String filePath, String content) {
        String[] parts = filePath.split("/");
        ProjectTreeView root = new ProjectTreeView();
        root.setId(UUID.randomUUID().toString());
        root.setLabel("");
        root.setChildren(new ArrayList<>());

        for (String part : parts) {
            insertNode(root, part, content);
        }

        return root;
    }

    private void insertNode(ProjectTreeView parent, String part, String content) {
        Optional<ProjectTreeView> opt = parent.getChildren()
                .stream()
                .filter(c -> c.getLabel().equals(part))
                .findFirst();

        ProjectTreeView child;
        if (opt.isPresent()) {
            child = opt.get();
        } else {
            child = new ProjectTreeView();
            child.setId(UUID.randomUUID().toString());
            child.setLabel(part);
            child.setChildren(new ArrayList<>());
            parent.getChildren().add(child);
        }

        if (isFile(part)) {
            child.setValue(content);
        }
    }

    private boolean isFile(String part) {
        return part.contains(".");
    }
}
