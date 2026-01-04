package com.springddd.domain.util;

import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class ReactiveTreeUtils {

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
}
