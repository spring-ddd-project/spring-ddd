package com.springddd.domain.util;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.Collectors;

public class ReactiveTreeUtils {

    /**
     * Builds a hierarchical tree structure from a flat list of nodes.
     *
     * @param flatList           The input flat list of all nodes.
     * @param idGetter           Function to extract the unique ID from a node.
     * @param parentIdGetter     Function to extract the parent ID from a node.
     * @param childrenSetter     BiConsumer to set the list of child nodes for a parent node.
     * @param isRootPredicate    Predicate to determine whether a node is a root.
     * @param sorter             Optional comparator for sorting nodes at each level.
     * @param filter             Optional predicate to filter nodes before tree building.
     * @param maxDepth           The maximum depth to build the tree.
     * @param isDeletedPredicate Predicate to identify and exclude deleted or disabled nodes and their subtrees.
     * @return A Mono emitting the root nodes of the constructed tree.
     */
    public static <T, ID> Mono<List<T>> buildTree(List<T> flatList,
                                                  Function<T, ID> idGetter,
                                                  Function<T, ID> parentIdGetter,
                                                  BiConsumer<T, List<T>> childrenSetter,
                                                  Predicate<T> isRootPredicate,
                                                  @Nullable Comparator<T> sorter,
                                                  @Nullable Predicate<T> filter,
                                                  int maxDepth,
                                                  Predicate<T> isDeletedPredicate) {

        return Mono.fromCallable(() -> {
            Set<ID> excludedIds = collectExcludedSubtreeIds(flatList, idGetter, parentIdGetter, isDeletedPredicate);

            Predicate<T> combinedFilter = node ->
                    (filter == null || filter.test(node)) && !excludedIds.contains(idGetter.apply(node));

            List<T> filteredList = flatList.stream()
                    .filter(combinedFilter)
                    .toList();

            Map<ID, T> idMap = new HashMap<>();
            Map<ID, List<T>> parentMap = new HashMap<>();

            for (T item : filteredList) {
                ID id = idGetter.apply(item);
                ID parentId = parentIdGetter.apply(item);
                idMap.put(id, item);
                parentMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(item);
                childrenSetter.accept(item, new ArrayList<>());
            }

            List<T> roots = filteredList.stream()
                    .filter(isRootPredicate)
                    .collect(Collectors.toList());

            if (sorter != null) {
                roots.sort(sorter);
            }

            for (T root : roots) {
                buildChildren(root, idGetter, childrenSetter, parentMap, sorter, maxDepth, 1);
            }

            return roots;
        });
    }

    /**
     * Recursively builds the child nodes of a given parent node.
     *
     * @param parent         The parent node.
     * @param idGetter       Function to get the node ID.
     * @param childrenSetter BiConsumer to set child nodes.
     * @param parentIdMap    Map of parent ID to their direct children.
     * @param sorter         Optional comparator for child node sorting.
     * @param maxDepth       Maximum allowed tree depth.
     * @param currentDepth   Current recursion depth.
     */
    private static <T, ID> void buildChildren(T parent,
                                              Function<T, ID> idGetter,
                                              BiConsumer<T, List<T>> childrenSetter,
                                              Map<ID, List<T>> parentIdMap,
                                              @Nullable Comparator<T> sorter,
                                              int maxDepth,
                                              int currentDepth) {
        if (currentDepth >= maxDepth) return;

        ID parentId = idGetter.apply(parent);
        List<T> children = parentIdMap.getOrDefault(parentId, Collections.emptyList());

        if (sorter != null) {
            children.sort(sorter);
        }

        childrenSetter.accept(parent, children);

        for (T child : children) {
            buildChildren(child, idGetter, childrenSetter, parentIdMap, sorter, maxDepth, currentDepth + 1);
        }
    }

    /**
     * Recursively loads all ancestor nodes (parent chain) of a given node in a reactive way.
     *
     * @param parentId       The ID of the current node’s parent.
     * @param nodeMap        Map for caching already loaded nodes to avoid redundant fetches.
     * @param parentLoader   Reactive function to load a node by its ID.
     * @param idGetter       Function to extract node ID.
     * @param parentIdGetter Function to extract parent ID.
     * @return Mono that completes when the entire parent chain is loaded.
     */
    public static <T, ID> Mono<Void> loadParentChain(
            ID parentId,
            Map<ID, T> nodeMap,
            Function<ID, Mono<T>> parentLoader,
            Function<T, ID> idGetter,
            Function<T, ID> parentIdGetter
    ) {
        if (parentId == null || nodeMap.containsKey(parentId)) {
            return Mono.empty();
        }

        return parentLoader.apply(parentId)
                .flatMap(parent -> {
                    ID id = idGetter.apply(parent);
                    nodeMap.put(id, parent);
                    ID nextParentId = parentIdGetter.apply(parent);
                    return loadParentChain(nextParentId, nodeMap, parentLoader, idGetter, parentIdGetter);
                });
    }

    /**
     * Loads missing parent nodes from a list and builds a complete tree structure.
     *
     * @param initialList        List of initial nodes (may not include all parents).
     * @param idGetter           Function to get node ID.
     * @param parentIdGetter     Function to get parent ID.
     * @param parentLoader       Function to load parent nodes reactively.
     * @param childrenSetter     Function to set child nodes on a parent.
     * @param isRootPredicate    Predicate to determine root nodes.
     * @param sorter             Optional comparator for sorting nodes.
     * @param filter             Optional predicate to filter nodes.
     * @param maxDepth           Maximum depth to build the tree.
     * @param isDeletedPredicate Predicate to identify and exclude deleted nodes and their subtrees.
     * @return A Mono emitting a list of root nodes forming the complete tree.
     */
    public static <T, ID> Mono<List<T>> loadParentsAndBuildTree(
            List<T> initialList,
            Function<T, ID> idGetter,
            Function<T, ID> parentIdGetter,
            Function<ID, Mono<T>> parentLoader,
            BiConsumer<T, List<T>> childrenSetter,
            Predicate<T> isRootPredicate,
            @Nullable Comparator<T> sorter,
            @Nullable Predicate<T> filter,
            int maxDepth,
            Predicate<T> isDeletedPredicate
    ) {
        if (CollectionUtils.isEmpty(initialList)) {
            return Mono.just(Collections.emptyList());
        }

        Map<ID, T> nodeMap = new ConcurrentHashMap<>();
        for (T item : initialList) {
            nodeMap.put(idGetter.apply(item), item);
        }

        return Flux.fromIterable(initialList)
                .flatMap(item -> loadParentChain(
                        parentIdGetter.apply(item),
                        nodeMap,
                        parentLoader,
                        idGetter,
                        parentIdGetter
                ))
                .then(Mono.fromCallable(() -> new ArrayList<>(nodeMap.values())))
                .flatMap(flatList -> buildTree(
                        flatList,
                        idGetter,
                        parentIdGetter,
                        childrenSetter,
                        isRootPredicate,
                        sorter,
                        filter,
                        maxDepth,
                        isDeletedPredicate
                ));
    }

    /**
     * Finds all descendant nodes (including the root itself) starting from a given root ID.
     *
     * @param rootId         ID of the root node.
     * @param flatList       The complete flat list of nodes.
     * @param idGetter       Function to extract node ID.
     * @param parentIdGetter Function to extract parent ID.
     * @return List of the root node and all its descendants.
     */
    public static <T, ID> List<T> findAllChildrenFrom(
            ID rootId,
            List<T> flatList,
            Function<T, ID> idGetter,
            Function<T, ID> parentIdGetter
    ) {
        if (rootId == null || CollectionUtils.isEmpty(flatList)) {
            return Collections.emptyList();
        }

        Map<ID, List<T>> parentMap = new HashMap<>();
        Map<ID, T> nodeMap = new HashMap<>();
        for (T node : flatList) {
            nodeMap.put(idGetter.apply(node), node);
            ID parentId = parentIdGetter.apply(node);
            parentMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(node);
        }

        List<T> result = new ArrayList<>();
        Set<ID> visited = new HashSet<>();

        T rootNode = nodeMap.get(rootId);
        if (rootNode != null) {
            collectChildren(rootNode, idGetter, parentMap, result, visited);
        }

        return result;
    }

    /**
     * Recursively collects all descendants of a given node.
     *
     * @param current   The current node being traversed.
     * @param idGetter  Function to get node ID.
     * @param parentMap Map of parent ID to their children.
     * @param result    List to accumulate all visited nodes.
     * @param visited   Set to track visited nodes and avoid infinite recursion.
     */
    private static <T, ID> void collectChildren(T current,
                                                Function<T, ID> idGetter,
                                                Map<ID, List<T>> parentMap,
                                                List<T> result,
                                                Set<ID> visited) {
        ID currentId = idGetter.apply(current);
        if (!visited.add(currentId)) return;

        result.add(current);

        List<T> children = parentMap.getOrDefault(currentId, Collections.emptyList());
        for (T child : children) {
            collectChildren(child, idGetter, parentMap, result, visited);
        }
    }

    /**
     * Builds a subtree rooted at a specific node, filtering out deleted nodes.
     *
     * @param rootId             ID of the root node.
     * @param flatList           Flat list of nodes to build from.
     * @param idGetter           Function to get node ID.
     * @param parentIdGetter     Function to get parent ID.
     * @param childrenSetter     Function to set children on a node.
     * @param sorter             Optional comparator for sorting children.
     * @param maxDepth           Maximum tree depth to build.
     * @param isDeletedPredicate Predicate to identify and exclude deleted nodes.
     * @return List of root node’s immediate children and their subtrees.
     */
    public static <T, ID> List<T> buildSubTreeFrom(
            ID rootId,
            List<T> flatList,
            Function<T, ID> idGetter,
            Function<T, ID> parentIdGetter,
            BiConsumer<T, List<T>> childrenSetter,
            @Nullable Comparator<T> sorter,
            int maxDepth,
            Predicate<T> isDeletedPredicate
    ) {
        if (rootId == null || CollectionUtils.isEmpty(flatList)) {
            return Collections.emptyList();
        }

        Set<ID> excludedIds = collectExcludedSubtreeIds(flatList, idGetter, parentIdGetter, isDeletedPredicate);

        Map<ID, List<T>> parentMap = new HashMap<>();
        for (T node : flatList) {
            ID parentId = parentIdGetter.apply(node);
            parentMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(node);
            childrenSetter.accept(node, new ArrayList<>());
        }

        List<T> roots = parentMap.getOrDefault(rootId, Collections.emptyList())
                .stream()
                .filter(node -> !excludedIds.contains(idGetter.apply(node)))
                .collect(Collectors.toList());

        if (sorter != null) {
            roots.sort(sorter);
        }

        for (T root : roots) {
            buildChildren(root, idGetter, childrenSetter, parentMap, sorter, maxDepth, 1);
        }

        return roots;
    }

    /**
     * Collects IDs of all nodes that should be excluded (e.g., deleted) and their entire subtrees.
     *
     * @param flatList           The complete flat list of nodes.
     * @param idGetter           Function to get node ID.
     * @param parentIdGetter     Function to get parent ID.
     * @param isDeletedPredicate Predicate to identify deleted or disabled nodes.
     * @return Set of IDs to be excluded from tree construction.
     */
    private static <T, ID> Set<ID> collectExcludedSubtreeIds(
            List<T> flatList,
            Function<T, ID> idGetter,
            Function<T, ID> parentIdGetter,
            Predicate<T> isDeletedPredicate
    ) {
        Map<ID, List<T>> parentMap = new HashMap<>();
        for (T node : flatList) {
            ID parentId = parentIdGetter.apply(node);
            parentMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(node);
        }

        Set<ID> excludedIds = new HashSet<>();
        for (T node : flatList) {
            if (!isDeletedPredicate.test(node)) {
                collectChildrenForExclusion(node, idGetter, parentMap, excludedIds);
            }
        }
        return excludedIds;
    }

    /**
     * Recursively collects all descendant node IDs starting from a deleted node.
     *
     * @param node        The starting (deleted) node.
     * @param idGetter    Function to extract node ID.
     * @param parentMap   Map of parent ID to children.
     * @param excludedIds Set collecting all IDs that should be excluded.
     */
    private static <T, ID> void collectChildrenForExclusion(
            T node,
            Function<T, ID> idGetter,
            Map<ID, List<T>> parentMap,
            Set<ID> excludedIds
    ) {
        ID nodeId = idGetter.apply(node);
        if (!excludedIds.add(nodeId)) return;

        List<T> children = parentMap.getOrDefault(nodeId, Collections.emptyList());
        for (T child : children) {
            collectChildrenForExclusion(child, idGetter, parentMap, excludedIds);
        }
    }
}
