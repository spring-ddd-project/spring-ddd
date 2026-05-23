package com.springddd.infrastructure.persistence.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.relational.core.query.Criteria;

import static org.assertj.core.api.Assertions.assertThat;

class CriteriaFlyweightFactoryTest {

    @Test
    @DisplayName("getDeleteStatusCriteria(true) 应返回 delete_status 为 true 的 Criteria")
    void getDeleteStatusCriteria_true_shouldReturnCorrectCriteria() {
        Criteria criteria = CriteriaFlyweightFactory.getDeleteStatusCriteria(true);
        assertThat(criteria).isNotNull();
    }

    @Test
    @DisplayName("getDeleteStatusCriteria(false) 应返回 delete_status 为 false 的 Criteria")
    void getDeleteStatusCriteria_false_shouldReturnCorrectCriteria() {
        Criteria criteria = CriteriaFlyweightFactory.getDeleteStatusCriteria(false);
        assertThat(criteria).isNotNull();
    }

    @Test
    @DisplayName("相同参数的 Criteria 应为同一实例（享元模式）")
    void getDeleteStatusCriteria_shouldReturnSameInstanceForSameParameter() {
        Criteria criteria1 = CriteriaFlyweightFactory.getDeleteStatusCriteria(true);
        Criteria criteria2 = CriteriaFlyweightFactory.getDeleteStatusCriteria(true);
        assertThat(criteria1).isSameAs(criteria2);
    }

    @Test
    @DisplayName("不同参数的 Criteria 应为不同实例")
    void getDeleteStatusCriteria_shouldReturnDifferentInstancesForDifferentParameters() {
        Criteria criteriaTrue = CriteriaFlyweightFactory.getDeleteStatusCriteria(true);
        Criteria criteriaFalse = CriteriaFlyweightFactory.getDeleteStatusCriteria(false);
        assertThat(criteriaTrue).isNotSameAs(criteriaFalse);
    }
}
