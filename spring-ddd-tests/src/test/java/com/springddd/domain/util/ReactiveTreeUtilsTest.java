package com.springddd.domain.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ReactiveTreeUtilsTest {

    static class TestNode {
        private Long id;
        private Long parentId;
        private String name;

        public TestNode(Long id, Long parentId, String name) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
        }

        public Long getId() { return id; }
        public Long getParentId() { return parentId; }
        public String getName() { return name; }
    }

    @Test
    void findAllChildrenFrom_shouldReturnEmptyForNullRootId() {
        List<TestNode> flatList = Arrays.asList(
            new TestNode(1L, null, "root"),
            new TestNode(2L, 1L, "child1")
        );

        List<TestNode> result = ReactiveTreeUtils.findAllChildrenFrom(
            null, flatList, TestNode::getId, TestNode::getParentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllChildrenFrom_shouldReturnEmptyForEmptyList() {
        List<TestNode> result = ReactiveTreeUtils.findAllChildrenFrom(
            1L, List.of(), TestNode::getId, TestNode::getParentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllChildrenFrom_shouldReturnRootAndAllDescendants() {
        List<TestNode> flatList = Arrays.asList(
            new TestNode(1L, null, "root"),
            new TestNode(2L, 1L, "child1"),
            new TestNode(3L, 1L, "child2"),
            new TestNode(4L, 2L, "grandchild1"),
            new TestNode(5L, 2L, "grandchild2"),
            new TestNode(6L, 3L, "grandchild3")
        );

        List<TestNode> result = ReactiveTreeUtils.findAllChildrenFrom(
            1L, flatList, TestNode::getId, TestNode::getParentId);

        assertEquals(6, result.size());
    }

    @Test
    void findAllChildrenFrom_shouldReturnOnlyChildrenOfSpecificNode() {
        List<TestNode> flatList = Arrays.asList(
            new TestNode(1L, null, "root"),
            new TestNode(2L, 1L, "child1"),
            new TestNode(3L, 1L, "child2"),
            new TestNode(4L, 2L, "grandchild1"),
            new TestNode(5L, 3L, "grandchild2")
        );

        List<TestNode> result = ReactiveTreeUtils.findAllChildrenFrom(
            2L, flatList, TestNode::getId, TestNode::getParentId);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(n -> n.getId().equals(2L)));
        assertTrue(result.stream().anyMatch(n -> n.getId().equals(4L)));
    }

    @Test
    void findAllChildrenFrom_shouldReturnEmptyWhenRootNotFound() {
        List<TestNode> flatList = Arrays.asList(
            new TestNode(1L, null, "root"),
            new TestNode(2L, 1L, "child1")
        );

        List<TestNode> result = ReactiveTreeUtils.findAllChildrenFrom(
            999L, flatList, TestNode::getId, TestNode::getParentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllChildrenFrom_shouldHandleDeepNesting() {
        List<TestNode> flatList = Arrays.asList(
            new TestNode(1L, null, "level0"),
            new TestNode(2L, 1L, "level1"),
            new TestNode(3L, 2L, "level2"),
            new TestNode(4L, 3L, "level3"),
            new TestNode(5L, 4L, "level4")
        );

        List<TestNode> result = ReactiveTreeUtils.findAllChildrenFrom(
            1L, flatList, TestNode::getId, TestNode::getParentId);

        assertEquals(5, result.size());
    }

    @Test
    void findAllChildrenFrom_shouldNotReturnSiblingsOfRoot() {
        List<TestNode> flatList = Arrays.asList(
            new TestNode(1L, null, "root1"),
            new TestNode(2L, null, "root2"),
            new TestNode(3L, 1L, "childOfRoot1"),
            new TestNode(4L, 2L, "childOfRoot2")
        );

        List<TestNode> result = ReactiveTreeUtils.findAllChildrenFrom(
            1L, flatList, TestNode::getId, TestNode::getParentId);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(n -> n.getParentId() == null || n.getParentId().equals(1L)));
    }
}
