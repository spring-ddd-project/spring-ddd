package com.springddd.domain.util;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ReactiveTreeUtilsTest {

    // Test helper class that acts like a tree node with proper equals/hashCode
    static class TestNode {
        Long id;
        Long parentId;
        String name;
        List<TestNode> children = new ArrayList<>();
        boolean deleted;

        TestNode(Long id, Long parentId, String name, boolean deleted) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
            this.deleted = deleted;
        }

        public Long getId() { return id; }
        public Long getParentId() { return parentId; }
        public String getName() { return name; }
        public boolean isDeleted() { return deleted; }
        public List<TestNode> getChildren() { return children; }
        public void setChildren(List<TestNode> children) { this.children = children; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestNode testNode = (TestNode) o;
            return Objects.equals(id, testNode.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    private TestNode createNode(Long id, Long parentId, String name) {
        return new TestNode(id, parentId, name, false);
    }

    private TestNode createDeletedNode(Long id, Long parentId, String name) {
        return new TestNode(id, parentId, name, true);
    }

    private BiConsumer<TestNode, List<TestNode>> childrenSetter() {
        return (node, children) -> node.setChildren(children);
    }

    private Function<TestNode, Long> idGetter() {
        return TestNode::getId;
    }

    private Function<TestNode, Long> parentIdGetter() {
        return TestNode::getParentId;
    }

    private Predicate<TestNode> isRootPredicate() {
        return node -> node.getParentId() == null || node.getParentId() == 0L;
    }

    private Predicate<TestNode> isDeletedPredicate() {
        return TestNode::isDeleted;
    }

    @Test
    void testBuildTreeWithEmptyList() {
        List<TestNode> flatList = Collections.emptyList();

        Mono<List<TestNode>> resultMono = ReactiveTreeUtils.buildTree(
                flatList,
                idGetter(),
                parentIdGetter(),
                childrenSetter(),
                isRootPredicate(),
                null,
                null,
                10,
                isDeletedPredicate()
        );

        StepVerifier.create(resultMono)
                .assertNext(roots -> assertTrue(roots.isEmpty()))
                .verifyComplete();
    }

    @Test
    void testFindAllChildrenFromWithNullRootId() {
        List<TestNode> flatList = Arrays.asList(
                createNode(1L, null, "Root"),
                createNode(2L, 1L, "Child1")
        );

        List<TestNode> result = ReactiveTreeUtils.findAllChildrenFrom(
                null, flatList, idGetter(), parentIdGetter()
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllChildrenFromWithEmptyList() {
        List<TestNode> result = ReactiveTreeUtils.findAllChildrenFrom(
                1L, Collections.emptyList(), idGetter(), parentIdGetter()
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void testBuildSubTreeFromWithNullRootId() {
        List<TestNode> flatList = Arrays.asList(
                createNode(1L, null, "Root"),
                createNode(2L, 1L, "Child1")
        );

        List<TestNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                null, flatList, idGetter(), parentIdGetter(),
                childrenSetter(), null, 10, isDeletedPredicate()
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void testBuildSubTreeFromWithEmptyList() {
        List<TestNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                1L, Collections.emptyList(), idGetter(), parentIdGetter(),
                childrenSetter(), null, 10, isDeletedPredicate()
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void testLoadParentChainWithNullParentId() {
        Map<Long, TestNode> nodeMap = new HashMap<>();
        AtomicInteger loadCount = new AtomicInteger(0);

        Function<Long, Mono<TestNode>> parentLoader = id -> {
            loadCount.incrementAndGet();
            return Mono.empty();
        };

        Mono<Void> resultMono = ReactiveTreeUtils.loadParentChain(
                null, nodeMap, parentLoader, idGetter(), parentIdGetter()
        );

        StepVerifier.create(resultMono)
                .verifyComplete();

        assertEquals(0, loadCount.get());
    }

    @Test
    void testLoadParentChainWithAlreadyLoadedNode() {
        Map<Long, TestNode> nodeMap = new HashMap<>();
        TestNode existingNode = createNode(1L, null, "AlreadyLoaded");
        nodeMap.put(1L, existingNode);

        AtomicInteger loadCount = new AtomicInteger(0);

        Function<Long, Mono<TestNode>> parentLoader = id -> {
            loadCount.incrementAndGet();
            return Mono.empty();
        };

        Mono<Void> resultMono = ReactiveTreeUtils.loadParentChain(
                1L, nodeMap, parentLoader, idGetter(), parentIdGetter()
        );

        StepVerifier.create(resultMono)
                .verifyComplete();

        assertEquals(0, loadCount.get());
    }

    @Test
    void testLoadParentsAndBuildTreeWithEmptyList() {
        Mono<List<TestNode>> resultMono = ReactiveTreeUtils.loadParentsAndBuildTree(
                Collections.emptyList(),
                idGetter(),
                parentIdGetter(),
                id -> Mono.empty(),
                childrenSetter(),
                isRootPredicate(),
                null,
                null,
                10,
                isDeletedPredicate()
        );

        StepVerifier.create(resultMono)
                .assertNext(roots -> assertTrue(roots.isEmpty()))
                .verifyComplete();
    }

    @Test
    void testFindAllChildrenFrom() {
        List<TestNode> flatList = Arrays.asList(
                createNode(1L, null, "Root"),
                createNode(2L, 1L, "Child1"),
                createNode(3L, 1L, "Child2"),
                createNode(4L, 2L, "Grandchild1"),
                createNode(5L, 4L, "GreatGrandchild1")
        );

        List<TestNode> result = ReactiveTreeUtils.findAllChildrenFrom(
                2L, flatList, idGetter(), parentIdGetter()
        );

        assertEquals(3, result.size());
        Set<Long> ids = result.stream().map(TestNode::getId).collect(Collectors.toSet());
        assertTrue(ids.contains(2L));
        assertTrue(ids.contains(4L));
        assertTrue(ids.contains(5L));
    }

    @Test
    void testFindAllChildrenFromWithNonExistentRootId() {
        List<TestNode> flatList = Arrays.asList(
                createNode(1L, null, "Root"),
                createNode(2L, 1L, "Child1")
        );

        List<TestNode> result = ReactiveTreeUtils.findAllChildrenFrom(
                999L, flatList, idGetter(), parentIdGetter()
        );

        assertTrue(result.isEmpty());
    }
}
