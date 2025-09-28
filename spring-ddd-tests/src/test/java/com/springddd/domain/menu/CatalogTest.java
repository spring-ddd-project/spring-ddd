package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {

    @Test
    void shouldCreateCatalogWithValidMenuRedirect() {
        Catalog catalog = new Catalog("/redirect");
        assertEquals("/redirect", catalog.menuRedirect());
    }

    @Test
    void shouldCreateCatalogWithEmptyString() {
        Catalog catalog = new Catalog("");
        assertEquals("", catalog.menuRedirect());
    }

    @Test
    void shouldCreateCatalogWithNull() {
        Catalog catalog = new Catalog(null);
        assertNull(catalog.menuRedirect());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        Catalog catalog1 = new Catalog("/redirect");
        Catalog catalog2 = new Catalog("/redirect");
        assertEquals(catalog1, catalog2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        Catalog catalog1 = new Catalog("/redirect1");
        Catalog catalog2 = new Catalog("/redirect2");
        assertNotEquals(catalog1, catalog2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        Catalog catalog1 = new Catalog("/redirect");
        Catalog catalog2 = new Catalog("/redirect");
        assertEquals(catalog1.hashCode(), catalog2.hashCode());
    }

    @Test
    void toString_shouldReturnValue() {
        Catalog catalog = new Catalog("/redirect");
        assertTrue(catalog.toString().contains("/redirect"));
    }
}
