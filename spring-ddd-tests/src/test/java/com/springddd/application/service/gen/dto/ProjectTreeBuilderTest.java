package com.springddd.application.service.gen.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectTreeBuilderTest {

    private final ProjectTreeBuilder builder = ProjectTreeBuilder.getInstance();

    @Test
    @DisplayName("getInstance 应返回单例")
    void getInstance_shouldReturnSingleton() {
        ProjectTreeBuilder instance1 = ProjectTreeBuilder.getInstance();
        ProjectTreeBuilder instance2 = ProjectTreeBuilder.getInstance();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    @DisplayName("buildTree 应从 null 根节点创建新树")
    void buildTree_shouldCreateNewTree() {
        ProjectTreeView root = builder.buildTree(null, "a/b/c.java", "content");

        assertThat(root.getLabel()).isEqualTo("a");
        // 代码实现中，parts[0] 既作为 root label 又在循环中作为第一个子节点创建
        assertThat(root.getChildren()).hasSize(1);
        assertThat(root.getChildren().get(0).getLabel()).isEqualTo("a");
        assertThat(root.getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("b");
        assertThat(root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("c.java");
        assertThat(root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getValue()).isEqualTo("content");
    }

    @Test
    @DisplayName("buildTree 应复用已有节点")
    void buildTree_shouldReuseExistingNodes() {
        ProjectTreeView root = builder.buildTree(null, "a/b/c.java", "content1");
        root = builder.buildTree(root, "a/b/d.java", "content2");

        // root("a") -> child("a") -> child("b") -> [child("c.java"), child("d.java")]
        assertThat(root.getChildren()).hasSize(1);
        assertThat(root.getChildren().get(0).getChildren()).hasSize(1);
        assertThat(root.getChildren().get(0).getChildren().get(0).getChildren()).hasSize(2);
        assertThat(root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("c.java");
        assertThat(root.getChildren().get(0).getChildren().get(0).getChildren().get(1).getLabel()).isEqualTo("d.java");
    }

    @Test
    @DisplayName("buildTree 目录节点不应有 value")
    void buildTree_directoryNode_shouldHaveNoValue() {
        ProjectTreeView root = builder.buildTree(null, "a/b/c.java", "content");

        assertThat(root.getValue()).isNull();
        assertThat(root.getChildren().get(0).getValue()).isNull();
        assertThat(root.getChildren().get(0).getChildren().get(0).getValue()).isNull();
        assertThat(root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getValue()).isEqualTo("content");
    }
}
