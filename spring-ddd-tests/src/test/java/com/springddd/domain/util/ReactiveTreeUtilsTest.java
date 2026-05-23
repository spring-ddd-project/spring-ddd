package com.springddd.domain.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

class ReactiveTreeUtilsTest {

    // ---------- Test Node & Helpers ----------

    static class TreeNode {
        Long id;
        Long parentId;
        String name;
        boolean deleted;
        List<TreeNode> children = new ArrayList<>();

        TreeNode(Long id, Long parentId, String name) {
            this(id, parentId, name, false);
        }

        TreeNode(Long id, Long parentId, String name, boolean deleted) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
            this.deleted = deleted;
        }
    }

    private final Function<TreeNode, Long> idGetter = n -> n.id;
    private final Function<TreeNode, Long> parentIdGetter = n -> n.parentId;
    private final BiConsumer<TreeNode, List<TreeNode>> childrenSetter = (n, c) -> n.children = c;
    private final Predicate<TreeNode> isDeleted = n -> n.deleted;
    private final Comparator<TreeNode> sorter = Comparator.comparing(n -> n.name);

    // ---------- buildSubTreeFrom ----------

    @Test
    @DisplayName("buildSubTreeFrom: basic subtree from root")
    void buildSubTreeFrom_basic() {
        TreeNode n1 = new TreeNode(1L, 0L, "A");
        TreeNode n2 = new TreeNode(2L, 1L, "B");
        TreeNode n3 = new TreeNode(3L, 1L, "C");
        TreeNode n4 = new TreeNode(4L, 2L, "D");
        List<TreeNode> flat = List.of(n1, n2, n3, n4);

        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                1L, flat, idGetter, parentIdGetter, childrenSetter, null, Integer.MAX_VALUE, isDeleted);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id).isEqualTo(2L);
        assertThat(result.get(0).children).hasSize(1);
        assertThat(result.get(0).children.get(0).id).isEqualTo(4L);
        assertThat(result.get(1).id).isEqualTo(3L);
    }

    @Test
    @DisplayName("buildSubTreeFrom: with deleted nodes and their subtrees excluded")
    void buildSubTreeFrom_withDeletedNodes() {
        TreeNode n1 = new TreeNode(1L, 0L, "A");
        TreeNode n2 = new TreeNode(2L, 1L, "B", true);  // deleted
        TreeNode n3 = new TreeNode(3L, 1L, "C");
        TreeNode n4 = new TreeNode(4L, 2L, "D");        // child of deleted
        List<TreeNode> flat = List.of(n1, n2, n3, n4);

        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                1L, flat, idGetter, parentIdGetter, childrenSetter, sorter, Integer.MAX_VALUE, isDeleted);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id).isEqualTo(3L);
    }

    @Test
    @DisplayName("buildSubTreeFrom: nested deleted nodes exclude all descendants")
    void buildSubTreeFrom_withNestedDeletedNodes() {
        TreeNode n1 = new TreeNode(1L, 0L, "A");
        TreeNode n2 = new TreeNode(2L, 1L, "B", true);  // deleted
        TreeNode n3 = new TreeNode(3L, 2L, "C");        // child of deleted
        TreeNode n4 = new TreeNode(4L, 3L, "D");        // grandchild of deleted
        List<TreeNode> flat = List.of(n1, n2, n3, n4);

        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                1L, flat, idGetter, parentIdGetter, childrenSetter, null, Integer.MAX_VALUE, isDeleted);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("buildSubTreeFrom: with maxDepth limits recursion")
    void buildSubTreeFrom_withMaxDepth() {
        TreeNode n1 = new TreeNode(1L, 0L, "A");
        TreeNode n2 = new TreeNode(2L, 1L, "B");
        TreeNode n3 = new TreeNode(3L, 2L, "C");
        TreeNode n4 = new TreeNode(4L, 3L, "D");
        List<TreeNode> flat = List.of(n1, n2, n3, n4);

        // maxDepth = 1 means only direct children of rootId, no deeper recursion
        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                1L, flat, idGetter, parentIdGetter, childrenSetter, null, 1, isDeleted);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id).isEqualTo(2L);
        assertThat(result.get(0).children).isEmpty(); // truncated by maxDepth
    }

    @Test
    @DisplayName("buildSubTreeFrom: with sorting")
    void buildSubTreeFrom_withSorting() {
        TreeNode n1 = new TreeNode(1L, 0L, "A");
        TreeNode n2 = new TreeNode(2L, 1L, "Z");
        TreeNode n3 = new TreeNode(3L, 1L, "A");
        TreeNode n4 = new TreeNode(4L, 1L, "M");
        List<TreeNode> flat = List.of(n1, n2, n3, n4);

        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                1L, flat, idGetter, parentIdGetter, childrenSetter, sorter, Integer.MAX_VALUE, isDeleted);

        assertThat(result).extracting(n -> n.name).containsExactly("A", "M", "Z");
    }

    @Test
    @DisplayName("buildSubTreeFrom: null rootId returns empty list")
    void buildSubTreeFrom_nullRootId() {
        List<TreeNode> flat = List.of(new TreeNode(1L, 0L, "A"));
        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                null, flat, idGetter, parentIdGetter, childrenSetter, null, Integer.MAX_VALUE, isDeleted);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("buildSubTreeFrom: empty flat list returns empty list")
    void buildSubTreeFrom_emptyList() {
        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                1L, Collections.emptyList(), idGetter, parentIdGetter, childrenSetter, null, Integer.MAX_VALUE, isDeleted);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("buildSubTreeFrom: rootId not in parentMap returns empty list")
    void buildSubTreeFrom_rootIdNotFound() {
        TreeNode n1 = new TreeNode(1L, 0L, "A");
        List<TreeNode> result = ReactiveTreeUtils.buildSubTreeFrom(
                99L, List.of(n1), idGetter, parentIdGetter, childrenSetter, null, Integer.MAX_VALUE, isDeleted);
        assertThat(result).isEmpty();
    }

    // ---------- buildTree (covers collectExcludedSubtreeIds & collectChildrenForExclusion) ----------

    @Test
    @DisplayName("buildTree: with deleted nodes excluded and their subtrees")
    void buildTree_withDeletedNodes() {
        TreeNode n1 = new TreeNode(1L, null, "A");
        TreeNode n2 = new TreeNode(2L, 1L, "B", true);
        TreeNode n3 = new TreeNode(3L, 2L, "C");
        TreeNode n4 = new TreeNode(4L, 1L, "D");
        List<TreeNode> flat = List.of(n1, n2, n3, n4);

        Predicate<TreeNode> isRoot = n -> n.parentId == null;

        StepVerifier.create(ReactiveTreeUtils.buildTree(
                        flat, idGetter, parentIdGetter, childrenSetter,
                        isRoot, null, null, Integer.MAX_VALUE, isDeleted))
                .assertNext(roots -> {
                    assertThat(roots).hasSize(1);
                    assertThat(roots.get(0).children).hasSize(1);
                    assertThat(roots.get(0).children.get(0).id).isEqualTo(4L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("buildTree: with filter predicate")
    void buildTree_withFilter() {
        TreeNode n1 = new TreeNode(1L, null, "A");
        TreeNode n2 = new TreeNode(2L, 1L, "B");
        TreeNode n3 = new TreeNode(3L, 1L, "C");
        List<TreeNode> flat = List.of(n1, n2, n3);

        Predicate<TreeNode> isRoot = n -> n.parentId == null;
        Predicate<TreeNode> filter = n -> !n.name.equals("B");

        StepVerifier.create(ReactiveTreeUtils.buildTree(
                        flat, idGetter, parentIdGetter, childrenSetter,
                        isRoot, sorter, filter, Integer.MAX_VALUE, n -> false))
                .assertNext(roots -> {
                    assertThat(roots.get(0).children).hasSize(1);
                    assertThat(roots.get(0).children.get(0).id).isEqualTo(3L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("buildTree: deleted nodes with cyclic references are handled")
    void buildTree_withDeletedCyclicNodes() {
        // Deleted node that is its own parent (cycle in exclusion collection)
        TreeNode n1 = new TreeNode(1L, null, "A");
        TreeNode n2 = new TreeNode(2L, 2L, "B", true);  // deleted, parent is itself
        List<TreeNode> flat = List.of(n1, n2);

        Predicate<TreeNode> isRoot = n -> n.parentId == null;

        StepVerifier.create(ReactiveTreeUtils.buildTree(
                        flat, idGetter, parentIdGetter, childrenSetter,
                        isRoot, null, null, Integer.MAX_VALUE, isDeleted))
                .assertNext(roots -> {
                    assertThat(roots).hasSize(1);
                    assertThat(roots.get(0).id).isEqualTo(1L);
                })
                .verifyComplete();
    }

    // ---------- loadParentsAndBuildTree ----------

    @Test
    @DisplayName("loadParentsAndBuildTree: empty list returns empty list")
    void loadParentsAndBuildTree_emptyList() {
        StepVerifier.create(ReactiveTreeUtils.loadParentsAndBuildTree(
                        Collections.emptyList(), idGetter, parentIdGetter,
                        id -> Mono.just(new TreeNode(id, null, "P")),
                        childrenSetter, n -> n.parentId == null,
                        null, null, Integer.MAX_VALUE, n -> false))
                .assertNext(result -> assertThat(result).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("loadParentsAndBuildTree: loads missing parents reactively")
    void loadParentsAndBuildTree_withParents() {
        TreeNode child = new TreeNode(2L, 1L, "Child");
        TreeNode parent = new TreeNode(1L, null, "Parent");

        Map<Long, TreeNode> db = new HashMap<>();
        db.put(1L, parent);

        Function<Long, Mono<TreeNode>> parentLoader = id -> Mono.justOrEmpty(db.get(id));
        Predicate<TreeNode> isRoot = n -> n.parentId == null;

        StepVerifier.create(ReactiveTreeUtils.loadParentsAndBuildTree(
                        List.of(child), idGetter, parentIdGetter, parentLoader,
                        childrenSetter, isRoot, null, null, Integer.MAX_VALUE, n -> false))
                .assertNext(roots -> {
                    assertThat(roots).hasSize(1);
                    assertThat(roots.get(0).id).isEqualTo(1L);
                    assertThat(roots.get(0).children).hasSize(1);
                    assertThat(roots.get(0).children.get(0).id).isEqualTo(2L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("loadParentsAndBuildTree: parent already in nodeMap is not reloaded")
    void loadParentsAndBuildTree_parentAlreadyLoaded() {
        TreeNode parent = new TreeNode(1L, null, "Parent");
        TreeNode child = new TreeNode(2L, 1L, "Child");

        // parentLoader returns null to simulate missing, but parent is already in initialList
        Function<Long, Mono<TreeNode>> parentLoader = id -> Mono.empty();
        Predicate<TreeNode> isRoot = n -> n.parentId == null;

        StepVerifier.create(ReactiveTreeUtils.loadParentsAndBuildTree(
                        List.of(parent, child), idGetter, parentIdGetter, parentLoader,
                        childrenSetter, isRoot, null, null, Integer.MAX_VALUE, n -> false))
                .assertNext(roots -> {
                    assertThat(roots).hasSize(1);
                    assertThat(roots.get(0).id).isEqualTo(1L);
                    assertThat(roots.get(0).children).hasSize(1);
                })
                .verifyComplete();
    }

    // ---------- findAllChildrenFrom ----------

    @Test
    @DisplayName("findAllChildrenFrom: collects root and all descendants")
    void findAllChildrenFrom_basic() {
        TreeNode n1 = new TreeNode(1L, 0L, "A");
        TreeNode n2 = new TreeNode(2L, 1L, "B");
        TreeNode n3 = new TreeNode(3L, 2L, "C");
        TreeNode n4 = new TreeNode(4L, 1L, "D");
        List<TreeNode> flat = List.of(n1, n2, n3, n4);

        List<TreeNode> result = ReactiveTreeUtils.findAllChildrenFrom(1L, flat, idGetter, parentIdGetter);

        assertThat(result).extracting(n -> n.id).containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
    }

    @Test
    @DisplayName("findAllChildrenFrom: null rootId returns empty list")
    void findAllChildrenFrom_nullRootId() {
        List<TreeNode> result = ReactiveTreeUtils.findAllChildrenFrom(
                null, List.of(new TreeNode(1L, 0L, "A")), idGetter, parentIdGetter);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAllChildrenFrom: empty flat list returns empty list")
    void findAllChildrenFrom_emptyList() {
        List<TreeNode> result = ReactiveTreeUtils.findAllChildrenFrom(
                1L, Collections.emptyList(), idGetter, parentIdGetter);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAllChildrenFrom: rootId not found in nodeMap returns empty list")
    void findAllChildrenFrom_rootIdNotFound() {
        TreeNode n1 = new TreeNode(1L, 0L, "A");
        List<TreeNode> result = ReactiveTreeUtils.findAllChildrenFrom(
                99L, List.of(n1), idGetter, parentIdGetter);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAllChildrenFrom: cycle detection prevents infinite loop")
    void findAllChildrenFrom_withCycle() {
        // Create a cyclic reference: 1 -> 2 -> 3 -> 1
        TreeNode n1 = new TreeNode(1L, 3L, "A");
        TreeNode n2 = new TreeNode(2L, 1L, "B");
        TreeNode n3 = new TreeNode(3L, 2L, "C");
        List<TreeNode> flat = List.of(n1, n2, n3);

        List<TreeNode> result = ReactiveTreeUtils.findAllChildrenFrom(1L, flat, idGetter, parentIdGetter);

        // Should collect nodes without infinite loop; exact result depends on parentMap
        // but at minimum it should not hang and should include visited nodes
        assertThat(result).isNotNull();
        assertThat(result.size()).isLessThanOrEqualTo(3);
    }

    // ---------- loadParentChain ----------

    @Test
    @DisplayName("loadParentChain: null parentId returns empty mono")
    void loadParentChain_nullParentId() {
        Map<Long, TreeNode> nodeMap = new HashMap<>();
        StepVerifier.create(ReactiveTreeUtils.loadParentChain(
                        null, nodeMap, id -> Mono.just(new TreeNode(id, null, "P")), idGetter, parentIdGetter))
                .verifyComplete();
    }

    @Test
    @DisplayName("loadParentChain: already cached parentId returns empty mono")
    void loadParentChain_alreadyCached() {
        Map<Long, TreeNode> nodeMap = new HashMap<>();
        nodeMap.put(1L, new TreeNode(1L, null, "P"));
        StepVerifier.create(ReactiveTreeUtils.loadParentChain(
                        1L, nodeMap, id -> Mono.just(new TreeNode(id, null, "P")), idGetter, parentIdGetter))
                .verifyComplete();
    }
}
