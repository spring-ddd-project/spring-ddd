package com.springddd.domain.util;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ReactiveTreeUtilsFullTest {

    static class TreeNode {
        private Long id;
        private Long parentId;
        private String name;
        private boolean deleted;
        private int order;
        private List<TreeNode> children = new ArrayList<>();

        TreeNode(Long id, Long parentId, String name, boolean deleted, int order) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
            this.deleted = deleted;
            this.order = order;
        }

        Long getId() { return id; }
        Long getParentId() { return parentId; }
        String getName() { return name; }
        boolean isDeleted() { return deleted; }
        int getOrder() { return order; }
        List<TreeNode> getChildren() { return children; }
        void setChildren(List<TreeNode> children) { this.children = children; }
    }

    @Test
    void buildTree_shouldReturnEmptyForEmptyList() {
        StepVerifier.create(ReactiveTreeUtils.buildTree(
                List.of(),
                TreeNode::getId,
                TreeNode::getParentId,
                TreeNode::setChildren,
                n -> n.getParentId() == null,
                Comparator.comparing(TreeNode::getOrder),
                null,
                10,
                TreeNode::isDeleted))
                .assertNext(List::isEmpty)
                .verifyComplete();
    }

    @Test
    void buildTree_shouldBuildSimpleTree() {
        TreeNode root = new TreeNode(1L, null, "root", false, 1);
        TreeNode child = new TreeNode(2L, 1L, "child", false, 1);

        StepVerifier.create(ReactiveTreeUtils.buildTree(
                List.of(root, child),
                TreeNode::getId,
                TreeNode::getParentId,
                TreeNode::setChildren,
                n -> n.getParentId() == null,
                Comparator.comparing(TreeNode::getOrder),
                null,
                10,
                TreeNode::isDeleted))
                .assertNext(result -> {
                    assertEquals(1, result.size());
                    assertEquals(1, result.get(0).getChildren().size());
                })
                .verifyComplete();
    }

    @Test
    void buildTree_shouldExcludeDeletedSubtrees() {
        TreeNode root = new TreeNode(1L, null, "root", false, 1);
        TreeNode deleted = new TreeNode(2L, 1L, "deleted", true, 1);
        TreeNode grandchild = new TreeNode(3L, 2L, "grandchild", false, 1);

        StepVerifier.create(ReactiveTreeUtils.buildTree(
                List.of(root, deleted, grandchild),
                TreeNode::getId,
                TreeNode::getParentId,
                TreeNode::setChildren,
                n -> n.getParentId() == null,
                null,
                null,
                10,
                TreeNode::isDeleted))
                .assertNext(result -> {
                    assertEquals(1, result.size());
                    assertTrue(result.get(0).getChildren().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void buildTree_shouldRespectMaxDepth() {
        TreeNode root = new TreeNode(1L, null, "root", false, 1);
        TreeNode child = new TreeNode(2L, 1L, "child", false, 1);
        TreeNode grandchild = new TreeNode(3L, 2L, "grandchild", false, 1);

        StepVerifier.create(ReactiveTreeUtils.buildTree(
                List.of(root, child, grandchild),
                TreeNode::getId,
                TreeNode::getParentId,
                TreeNode::setChildren,
                n -> n.getParentId() == null,
                null,
                null,
                2,
                TreeNode::isDeleted))
                .assertNext(result -> {
                    assertEquals(1, result.get(0).getChildren().size());
                    assertTrue(result.get(0).getChildren().get(0).getChildren().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void buildTree_shouldApplyFilter() {
        TreeNode root = new TreeNode(1L, null, "root", false, 1);
        TreeNode child = new TreeNode(2L, 1L, "child", false, 1);

        StepVerifier.create(ReactiveTreeUtils.buildTree(
                List.of(root, child),
                TreeNode::getId,
                TreeNode::getParentId,
                TreeNode::setChildren,
                n -> n.getParentId() == null,
                null,
                n -> n.getId().equals(1L),
                10,
                TreeNode::isDeleted))
                .assertNext(result -> {
                    assertEquals(1, result.size());
                    assertTrue(result.get(0).getChildren().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void loadParentChain_shouldReturnEmptyForNullParentId() {
        Map<Long, TreeNode> nodeMap = new HashMap<>();
        StepVerifier.create(ReactiveTreeUtils.loadParentChain(
                null, nodeMap, id -> Mono.empty(), TreeNode::getId, TreeNode::getParentId))
                .verifyComplete();
    }

    @Test
    void loadParentChain_shouldReturnEmptyForCachedNode() {
        TreeNode node = new TreeNode(1L, null, "root", false, 1);
        Map<Long, TreeNode> nodeMap = new HashMap<>();
        nodeMap.put(1L, node);
        StepVerifier.create(ReactiveTreeUtils.loadParentChain(
                1L, nodeMap, id -> Mono.empty(), TreeNode::getId, TreeNode::getParentId))
                .verifyComplete();
    }

    @Test
    void loadParentChain_shouldLoadParentsRecursively() {
        TreeNode parent = new TreeNode(1L, null, "parent", false, 1);
        TreeNode child = new TreeNode(2L, 1L, "child", false, 1);
        Map<Long, TreeNode> nodeMap = new HashMap<>();
        nodeMap.put(2L, child);

        StepVerifier.create(ReactiveTreeUtils.loadParentChain(
                1L, nodeMap, id -> Mono.just(parent), TreeNode::getId, TreeNode::getParentId))
                .verifyComplete();

        assertTrue(nodeMap.containsKey(1L));
    }

    @Test
    void loadParentsAndBuildTree_shouldReturnEmptyForEmptyList() {
        StepVerifier.create(ReactiveTreeUtils.loadParentsAndBuildTree(
                List.of(),
                TreeNode::getId,
                TreeNode::getParentId,
                id -> Mono.empty(),
                TreeNode::setChildren,
                n -> n.getParentId() == null,
                null,
                null,
                10,
                TreeNode::isDeleted))
                .assertNext(List::isEmpty)
                .verifyComplete();
    }

    @Test
    void loadParentsAndBuildTree_shouldLoadMissingParentsAndBuildTree() {
        TreeNode child = new TreeNode(2L, 1L, "child", false, 1);
        TreeNode parent = new TreeNode(1L, null, "parent", false, 1);

        StepVerifier.create(ReactiveTreeUtils.loadParentsAndBuildTree(
                List.of(child),
                TreeNode::getId,
                TreeNode::getParentId,
                id -> Mono.just(parent),
                TreeNode::setChildren,
                n -> n.getParentId() == null,
                null,
                null,
                10,
                TreeNode::isDeleted))
                .assertNext(result -> {
                    assertEquals(1, result.size());
                    assertEquals(1L, result.get(0).getId());
                    assertEquals(1, result.get(0).getChildren().size());
                })
                .verifyComplete();
    }

    @Test
    void buildSubTreeFrom_shouldReturnEmptyForNullRootId() {
        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                null, List.of(), TreeNode::getId, TreeNode::getParentId,
                TreeNode::setChildren, null, 10, TreeNode::isDeleted);
        assertTrue(result.isEmpty());
    }

    @Test
    void buildSubTreeFrom_shouldReturnEmptyForEmptyList() {
        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                1L, List.of(), TreeNode::getId, TreeNode::getParentId,
                TreeNode::setChildren, null, 10, TreeNode::isDeleted);
        assertTrue(result.isEmpty());
    }

    @Test
    void buildSubTreeFrom_shouldBuildSubTree() {
        TreeNode root = new TreeNode(1L, null, "root", false, 1);
        TreeNode child = new TreeNode(2L, 1L, "child", false, 1);
        TreeNode grandchild = new TreeNode(3L, 2L, "grandchild", false, 1);

        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                1L, List.of(root, child, grandchild), TreeNode::getId, TreeNode::getParentId,
                TreeNode::setChildren, Comparator.comparing(TreeNode::getOrder), 10, TreeNode::isDeleted);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals(1, result.get(0).getChildren().size());
    }

    @Test
    void buildSubTreeFrom_shouldExcludeDeletedNodes() {
        TreeNode root = new TreeNode(1L, null, "root", false, 1);
        TreeNode child = new TreeNode(2L, 1L, "child", true, 1);
        TreeNode grandchild = new TreeNode(3L, 2L, "grandchild", false, 1);

        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                1L, List.of(root, child, grandchild), TreeNode::getId, TreeNode::getParentId,
                TreeNode::setChildren, null, 10, TreeNode::isDeleted);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllChildrenFrom_shouldHandleCircularReferences() {
        TreeNode a = new TreeNode(1L, 2L, "a", false, 1);
        TreeNode b = new TreeNode(2L, 1L, "b", false, 1);

        List<TreeNode> result = ReactiveTreeUtils.findAllChildrenFrom(
                1L, List.of(a, b), TreeNode::getId, TreeNode::getParentId);

        assertEquals(2, result.size());
    }
}
