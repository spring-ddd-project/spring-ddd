package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTreeBuilderTest {

    private final ProjectTreeBuilder builder = new ProjectTreeBuilder();

    @Test
    void buildTree_shouldCreateRoot_whenRootIsNull() {
        ProjectTreeView result = builder.buildTree(null, "project/src/File.java", "content");

        assertNotNull(result);
        assertEquals("project", result.getLabel());
        assertEquals(1, result.getChildren().size());
    }

    @Test
    void buildTree_shouldAddFileWithContent() {
        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("test-project");
        root.setChildren(new java.util.ArrayList<>());

        ProjectTreeView result = builder.buildTree(root, "test-project/src/File.java", "public class File {}");

        assertEquals("test-project", result.getLabel());
        ProjectTreeView duplicateRoot = result.getChildren().get(0);
        assertEquals("test-project", duplicateRoot.getLabel());
        ProjectTreeView srcNode = duplicateRoot.getChildren().get(0);
        assertEquals("src", srcNode.getLabel());
        ProjectTreeView fileNode = srcNode.getChildren().get(0);
        assertEquals("File.java", fileNode.getLabel());
        assertEquals("public class File {}", fileNode.getValue());
    }

    @Test
    void buildTree_shouldReuseExistingNodes() {
        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("project");
        root.setChildren(new java.util.ArrayList<>());

        builder.buildTree(root, "project/src/File1.java", "content1");
        builder.buildTree(root, "project/src/File2.java", "content2");

        ProjectTreeView srcNode = root.getChildren().get(0).getChildren().get(0);
        assertEquals(2, srcNode.getChildren().size());
    }

    @Test
    void buildTree_shouldNotSetValueForDirectories() {
        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("project");
        root.setChildren(new java.util.ArrayList<>());

        ProjectTreeView result = builder.buildTree(root, "project/src/main", null);

        ProjectTreeView mainNode = result.getChildren().get(0).getChildren().get(0).getChildren().get(0);
        assertEquals("main", mainNode.getLabel());
        assertNull(mainNode.getValue());
    }
}
