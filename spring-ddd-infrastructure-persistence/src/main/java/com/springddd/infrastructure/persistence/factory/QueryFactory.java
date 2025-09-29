package com.springddd.infrastructure.persistence.factory;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;

/**
 * Factory for query templates.
 */
public interface QueryFactory extends InfrastructureFactory {
    R2dbcEntityTemplate getR2dbcEntityTemplate();
    DatabaseClient getDatabaseClient();
}
