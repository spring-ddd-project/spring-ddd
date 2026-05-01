package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {

    @Test
    void constructor_shouldSetMenuRedirect() {
        String redirect = "/home";
        Catalog catalog = new Catalog(redirect);
        assertEquals(redirect, catalog.menuRedirect());
    }

    @Test
    void constructor_shouldHandleEmptyRedirect() {
        Catalog catalog = new Catalog("");
        assertEquals("", catalog.menuRedirect());
    }

    @Test
    void constructor_shouldHandleNullRedirect() {
        Catalog catalog = new Catalog(null);
        assertNull(catalog.menuRedirect());
    }

    @Test
    void menuRedirect_shouldBeConsistent() {
        String testRedirect = "/test/path";
        Catalog catalog = new Catalog(testRedirect);
        assertEquals(catalog.menuRedirect(), testRedirect);
    }
}
