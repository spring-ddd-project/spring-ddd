package com.springddd.infrastructure.persistence.factory.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DefaultQueryFactoryTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private DatabaseClient databaseClient;

    private DefaultQueryFactory factory;

    @BeforeEach
    void setUp() {
        factory = new DefaultQueryFactory(r2dbcEntityTemplate, databaseClient);
    }

    @Test
    @DisplayName("getR2dbcEntityTemplate 应返回正确的模板实例")
    void getR2dbcEntityTemplate_shouldReturnCorrectTemplate() {
        assertThat(factory.getR2dbcEntityTemplate()).isSameAs(r2dbcEntityTemplate);
    }

    @Test
    @DisplayName("getDatabaseClient 应返回正确的客户端实例")
    void getDatabaseClient_shouldReturnCorrectClient() {
        assertThat(factory.getDatabaseClient()).isSameAs(databaseClient);
    }
}
