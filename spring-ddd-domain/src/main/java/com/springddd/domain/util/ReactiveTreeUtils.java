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
     * Builds a hierarchical tree from a flat list of nodes.
     *
     * @param flatList        The flat list of nodes.
     * @param idGetter        Function to get the ID of a node.
     * @param parentIdGetter  Function to get the parent ID of a node.
     * @param childrenSetter  Function to set the children of a node.
     * @param isRootPredicate Predicate to determine if a node is a root.
     * @param sorter          Optional comparator to sort child nodes.
     * @param filter          Optional filter predicate to exclude nodes.
     * @param maxDepth        Maximum depth of the tree (starting from 1).
     * @param <T>             Node type.
     * @param <ID>            ID type.
     * @return A Mono emitting the root nodes of the built tree.
     */
    public static <T, ID> Mono<List<T>> buildTree(List<T> flatList,
                                                  Function<T, ID> idGetter,
                                                  Function<T, ID> parentIdGetter,
                                                  BiConsumer<T, List<T>> childrenSetter,
                                                  Predicate<T> isRootPredicate,
                                                  @Nullable Comparator<T> sorter,
                                                  @Nullable Predicate<T> filter,
                                                  int maxDepth) {

        return Mono.fromCallable(() -> {
            // 1. Filter
            List<T> filteredList = filter == null
                    ? flatList
                    : flatList.stream().filter(filter).toList();

            // 2. Preprocessing: build mapping structures
            Map<ID, T> idMap = new HashMap<>();
            Map<ID, List<T>> parentMap = new HashMap<>();

            for (T item : filteredList) {
                ID id = idGetter.apply(item);
                ID parentId = parentIdGetter.apply(item);
                idMap.put(id, item);
                parentMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(item);
                childrenSetter.accept(item, new ArrayList<>());
            }

            // 3. Find root nodes
            List<T> roots = filteredList.stream()
                    .filter(isRootPredicate)
                    .collect(Collectors.toList());

            if (sorter != null) {
                roots.sort(sorter);
            }

            // 4. build tree
            for (T root : roots) {
                buildChildren(root, idGetter, childrenSetter, parentMap, sorter, maxDepth, 1);
            }

            return roots;
        });
    }

    /**
     * Recursively builds and assigns child nodes to the given parent node.
     *
     * @param parent         The current parent node.
     * @param idGetter       Function to get the ID of a node.
     * @param childrenSetter Function to set the children of a node.
     * @param parentIdMap    A map of parent ID to child node list.
     * @param sorter         Optional comparator for sorting children.
     * @param maxDepth       Maximum depth to recurse.
     * @param currentDepth   Current recursion depth.
     * @param <T>            Node type.
     * @param <ID>           ID type.
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
     * Recursively loads the given node's ancestors (parent chain) into the provided map.
     *
     * @param parentId       The ID of the parent node to load.
     * @param nodeMap        Cache map to hold loaded nodes, avoiding duplicates.
     * @param parentLoader   Function to load a node by its ID.
     * @param idGetter       Function to get a node's ID.
     * @param parentIdGetter Function to get a node's parent ID.
     * @param <T>            Node type.
     * @param <ID>           ID type.
     * @return A Mono signaling completion of the load process.
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
     * Loads all ancestors for a given list of nodes, then builds a tree structure.
     *
     * @param initialList     Initial list of nodes.
     * @param idGetter        Function to get a node's ID.
     * @param parentIdGetter  Function to get a node's parent ID.
     * @param parentLoader    Function to load a node by its ID.
     * @param childrenSetter  Function to set children on a node.
     * @param isRootPredicate Predicate to identify root nodes.
     * @param sorter          Optional comparator to sort child nodes.
     * @param filter          Optional filter predicate to exclude nodes.
     * @param maxDepth        Maximum depth for the resulting tree.
     * @param <T>             Node type.
     * @param <ID>            ID type.
     * @return A Mono emitting the list of root nodes after building the tree.
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
            int maxDepth
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
                        maxDepth
                ));
    }

    /**
     * Finds all descendant nodes (including the root itself) from a given root ID in a flat list.
     *
     * @param rootId         The starting node ID.
     * @param flatList       The flat list of all nodes.
     * @param idGetter       Function to get a node's ID.
     * @param parentIdGetter Function to get a node's parent ID.
     * @param <T>            Node type.
     * @param <ID>           ID type.
     * @return A flat list of all descendants including the root node.
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
     * Recursively collects the given node and all its children into the result list.
     *
     * @param current   The current node being processed.
     * @param idGetter  Function to get the node's ID.
     * @param parentMap Map of parent ID to child node list.
     * @param result    Accumulator list to collect results.
     * @param visited   Set to track visited node IDs (prevents cycles).
     * @param <T>       Node type.
     * @param <ID>      ID type.
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
     * Builds a subtree starting from the specified root ID.
     *
     * @param rootId         The root node ID to start from.
     * @param flatList       Flat list of all nodes.
     * @param idGetter       Function to get a node's ID.
     * @param parentIdGetter Function to get a node's parent ID.
     * @param childrenSetter Function to assign children to a node.
     * @param sorter         Optional comparator for sorting children.
     * @param maxDepth       Maximum tree depth from the root (inclusive).
     * @param <T>            Node type.
     * @param <ID>           ID type.
     * @return A list of root nodes representing the built subtree.
     */
    public static <T, ID> List<T> buildSubTreeFrom(
            ID rootId,
            List<T> flatList,
            Function<T, ID> idGetter,
            Function<T, ID> parentIdGetter,
            BiConsumer<T, List<T>> childrenSetter,
            @Nullable Comparator<T> sorter,
            int maxDepth
    ) {
        if (rootId == null || CollectionUtils.isEmpty(flatList)) {
            return Collections.emptyList();
        }

        Map<ID, List<T>> parentMap = new HashMap<>();
        for (T node : flatList) {
            ID parentId = parentIdGetter.apply(node);
            parentMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(node);
            childrenSetter.accept(node, new ArrayList<>());
        }

        List<T> roots = parentMap.getOrDefault(rootId, Collections.emptyList());
        if (sorter != null) {
            roots.sort(sorter);
        }

        for (T root : roots) {
            buildChildren(root, idGetter, childrenSetter, parentMap, sorter, maxDepth, 1);
        }

        return roots;
    }

}
