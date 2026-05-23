package com.springddd.application.service.gen.interpreter;

import com.springddd.application.service.gen.dto.ProjectTreeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathExpressionTest {

    @Test
    @DisplayName("ExactPathExpression 应精确匹配子节点")
    void exactPathExpression_shouldMatchExactLabel() {
        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("root");

        ProjectTreeView childA = new ProjectTreeView();
        childA.setLabel("A");
        ProjectTreeView childB = new ProjectTreeView();
        childB.setLabel("B");

        root.setChildren(List.of(childA, childB));

        ExactPathExpression expr = new ExactPathExpression("A");
        List<ProjectTreeView> result = expr.interpret(root);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLabel()).isEqualTo("A");
    }

    @Test
    @DisplayName("ExactPathExpression 无匹配时应返回空列表")
    void exactPathExpression_shouldReturnEmptyWhenNoMatch() {
        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("root");
        root.setChildren(List.of());

        ExactPathExpression expr = new ExactPathExpression("X");
        List<ProjectTreeView> result = expr.interpret(root);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("ExactPathExpression 无子节点时应返回空列表")
    void exactPathExpression_shouldReturnEmptyWhenNoChildren() {
        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("root");
        root.setChildren(null);

        ExactPathExpression expr = new ExactPathExpression("A");
        List<ProjectTreeView> result = expr.interpret(root);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("WildcardPathExpression 应返回所有子节点")
    void wildcardPathExpression_shouldReturnAllChildren() {
        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("root");

        ProjectTreeView childA = new ProjectTreeView();
        childA.setLabel("A");
        ProjectTreeView childB = new ProjectTreeView();
        childB.setLabel("B");

        root.setChildren(List.of(childA, childB));

        WildcardPathExpression expr = new WildcardPathExpression();
        List<ProjectTreeView> result = expr.interpret(root);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("WildcardPathExpression 无子节点时应返回空列表")
    void wildcardPathExpression_shouldReturnEmptyWhenNoChildren() {
        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("root");
        root.setChildren(null);

        WildcardPathExpression expr = new WildcardPathExpression();
        List<ProjectTreeView> result = expr.interpret(root);

        assertThat(result).isEmpty();
    }
}
