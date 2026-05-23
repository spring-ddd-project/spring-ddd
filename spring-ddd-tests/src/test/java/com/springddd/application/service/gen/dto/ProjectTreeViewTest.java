package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectTreeViewTest {

    @Test
    @DisplayName("ProjectTreeView 应支持 getter/setter")
    void projectTreeView_shouldSupportGetterSetter() {
        ProjectTreeView node = new ProjectTreeView();
        node.setLabel("root");
        node.setValue("val");
        node.setChildren(new ArrayList<>());

        assertThat(node.getLabel()).isEqualTo("root");
        assertThat(node.getValue()).isEqualTo("val");
        assertThat(node.getChildren()).isEmpty();
    }

    @Test
    @DisplayName("accept 应遍历所有节点")
    void accept_shouldTraverseAllNodes() {
        ProjectTreeView root = createTree();

        List<String> visited = new ArrayList<>();
        root.accept(node -> visited.add(node.getLabel()));

        assertThat(visited).containsExactly("root", "A", "A1", "A2", "B");
    }

    @Test
    @DisplayName("iterator 应按深度优先遍历")
    void iterator_shouldTraverseDepthFirst() {
        ProjectTreeView root = createTree();

        List<String> labels = new ArrayList<>();
        Iterator<ProjectTreeView> it = root.iterator();
        while (it.hasNext()) {
            labels.add(it.next().getLabel());
        }

        assertThat(labels).containsExactly("root", "A", "A1", "A2", "B");
    }

    private ProjectTreeView createTree() {
        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("root");

        ProjectTreeView a = new ProjectTreeView();
        a.setLabel("A");
        ProjectTreeView a1 = new ProjectTreeView();
        a1.setLabel("A1");
        ProjectTreeView a2 = new ProjectTreeView();
        a2.setLabel("A2");
        a.setChildren(new ArrayList<>(List.of(a1, a2)));

        ProjectTreeView b = new ProjectTreeView();
        b.setLabel("B");

        root.setChildren(new ArrayList<>(List.of(a, b)));
        return root;
    }
}
