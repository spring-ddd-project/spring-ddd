package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {

    @Test
    void shouldCreateCatalogWithMenuRedirect() {
        String menuRedirect = "/index";
        Catalog catalog = new Catalog(menuRedirect);
        assertEquals(menuRedirect, catalog.menuRedirect());
    }

    @Test
    void shouldHandleNullMenuRedirect() {
        Catalog catalog = new Catalog(null);
        assertNull(catalog.menuRedirect());
    }
}
