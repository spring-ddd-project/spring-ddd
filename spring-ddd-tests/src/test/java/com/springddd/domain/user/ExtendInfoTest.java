package com.springddd.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExtendInfoTest {

    @Test
    void shouldCreateExtendInfoWithAllFields() {
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("https://example.com/avatar.png");
        extendInfo.setSex(true);

        assertEquals("https://example.com/avatar.png", extendInfo.getAvatar());
        assertTrue(extendInfo.getSex());
    }

    @Test
    void shouldSetAndGetAvatar() {
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("/path/to/avatar.jpg");
        assertEquals("/path/to/avatar.jpg", extendInfo.getAvatar());
    }

    @Test
    void shouldSetAndGetSex() {
        ExtendInfo extendInfo = new ExtendInfo();

        extendInfo.setSex(true);
        assertTrue(extendInfo.getSex());

        extendInfo.setSex(false);
        assertFalse(extendInfo.getSex());
    }

    @Test
    void shouldHandleNullAvatar() {
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar(null);
        assertNull(extendInfo.getAvatar());
    }

    @Test
    void shouldHandleNullSex() {
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setSex(null);
        assertNull(extendInfo.getSex());
    }
}
