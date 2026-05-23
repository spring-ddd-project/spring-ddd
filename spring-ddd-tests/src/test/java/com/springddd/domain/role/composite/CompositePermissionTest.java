package com.springddd.domain.role.composite;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CompositePermissionTest {

    @Test
    void testEmptyComposite() {
        CompositePermission composite = new CompositePermission();
        assertThat(composite.isAuthorized(null)).isTrue();
    }

    @Test
    void testSingleAuthorized() {
        CompositePermission composite = new CompositePermission();
        composite.add(ctx -> true);
        assertThat(composite.isAuthorized(null)).isTrue();
    }

    @Test
    void testSingleNotAuthorized() {
        CompositePermission composite = new CompositePermission();
        composite.add(ctx -> false);
        assertThat(composite.isAuthorized(null)).isFalse();
    }

    @Test
    void testAllAuthorized() {
        CompositePermission composite = new CompositePermission();
        composite.add(ctx -> true);
        composite.add(ctx -> true);
        assertThat(composite.isAuthorized(null)).isTrue();
    }

    @Test
    void testOneNotAuthorized() {
        CompositePermission composite = new CompositePermission();
        composite.add(ctx -> true);
        composite.add(ctx -> false);
        assertThat(composite.isAuthorized(null)).isFalse();
    }
}
