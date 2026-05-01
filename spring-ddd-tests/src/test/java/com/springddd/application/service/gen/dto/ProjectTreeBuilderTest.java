package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTreeBuilderTest {

    private ProjectTreeBuilder builder = new ProjectTreeBuilder();

    @Test
    void buildTree_shouldCreateNewRoot_whenRootIsNull() {
        String filePath = "src/main/java/Test.java";
        String content = "public class Test {}";

        ProjectTreeView result = builder.buildTree(null, filePath, content);

        assertNotNull(result);
        assertEquals("src", result.getLabel());
        assertNotNull(result.getChildren());
        assertEquals(1, result.getChildren().size());
    }

    @Test
    void buildTree_shouldAppendToExistingRoot_whenRootProvided() {
        ProjectTreeView existingRoot = new ProjectTreeView();
        existingRoot.setLabel("src");
        existingRoot.setChildren(new ArrayList<>());

        String filePath = "src/main/java/Test.java";
        String content = "public class Test {}";

        ProjectTreeView result = builder.buildTree(existingRoot, filePath, content);

        assertEquals("src", result.getLabel());
        assertEquals(1, result.getChildren().size());
    }

    @Test
    void buildTree_shouldSetValue_whenLastPartIsFile() {
        String filePath = "Test.java";
        String content = "public class Test {}";

        ProjectTreeView result = builder.buildTree(null, filePath, content);

        assertEquals("Test.java", result.getLabel());
        assertEquals(content, result.getValue());
    }

    @Test
    void buildTree_shouldNotSetValue_whenLastPartIsDirectory() {
        String filePath = "folder";

        ProjectTreeView result = builder.buildTree(null, filePath, "");

        assertEquals("folder", result.getLabel());
        assertNull(result.getValue());
    }

    @Test
    void buildTree_shouldReuseExistingChild_whenPathMatches() {
        ProjectTreeView existingRoot = new ProjectTreeView();
        existingRoot.setLabel("src");
        existingRoot.setChildren(new ArrayList<>());

        ProjectTreeView existingChild = new ProjectTreeView();
        existingChild.setLabel("main");
        existingChild.setChildren(new ArrayList<>());
        existingRoot.getChildren().add(existingChild);

        String filePath = "src/main/java/Test.java";
        String content = "public class Test {}";

        ProjectTreeView result = builder.buildTree(existingRoot, filePath, content);

        assertEquals("src", result.getLabel());
        assertEquals(1, result.getChildren().size());
        assertEquals("main", result.getChildren().get(0).getLabel());
    }
}
