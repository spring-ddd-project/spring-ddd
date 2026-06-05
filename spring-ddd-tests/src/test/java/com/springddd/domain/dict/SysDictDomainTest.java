package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDictDomainTest {

    @Test
    void shouldCreateSysDictDomain() {
        SysDictDomain domain = new SysDictDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetId() {
        SysDictDomain domain = new SysDictDomain();
        DictId id = new DictId(1L);
        domain.setDictId(id);
        assertEquals(id, domain.getDictId());
    }

    @Test
    void shouldSetAndGetBasicInfo() {
        SysDictDomain domain = new SysDictDomain();
        DictBasicInfo basicInfo = new DictBasicInfo("dictName", "dictCode");
        domain.setDictBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getDictBasicInfo());
    }

    @Test
    void shouldSetAndGetExtendInfo() {
        SysDictDomain domain = new SysDictDomain();
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);
        domain.setDictExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getDictExtendInfo());
    }

    @Test
    void shouldCallCreate() {
        SysDictDomain domain = new SysDictDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void shouldUpdateSysDictDomain() {
        SysDictDomain domain = new SysDictDomain();
        DictBasicInfo basicInfo = new DictBasicInfo("newName", "newCode");
        DictExtendInfo extendInfo = new DictExtendInfo(2, false);

        domain.update(basicInfo, extendInfo);

        assertEquals(basicInfo, domain.getDictBasicInfo());
        assertEquals(extendInfo, domain.getDictExtendInfo());
    }

    @Test
    void shouldSetDeleteStatusOnDelete() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldClearDeleteStatusOnRestore() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(true);
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SysDictDomain domain = new SysDictDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysDictDomain"));
    }

    @Test
    void shouldDeleteWhenStateIsNullAndDeleteStatusFalse() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(false);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
        assertNotNull(domain.getState());
    }

    @Test
    void shouldDeleteWhenStateIsNullAndDeleteStatusTrue() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(true);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
        assertNotNull(domain.getState());
    }

    @Test
    void shouldDeleteWhenStateExists() {
        SysDictDomain domain = new SysDictDomain();
        domain.setState(new com.springddd.domain.dict.state.ActiveDictState());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldRestoreWhenStateIsNullAndDeleteStatusFalse() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(false);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
        assertNotNull(domain.getState());
    }

    @Test
    void shouldRestoreWhenStateIsNullAndDeleteStatusTrue() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(true);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
        assertNotNull(domain.getState());
    }

    @Test
    void shouldRestoreWhenStateExists() {
        SysDictDomain domain = new SysDictDomain();
        domain.setState(new com.springddd.domain.dict.state.DeletedDictState());
        domain.setDeleteStatus(true);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCloneWithNullFields() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(null);
        domain.setDictBasicInfo(null);
        domain.setDictExtendInfo(null);

        SysDictDomain clone = domain.clone();

        assertNotNull(clone);
        assertNull(clone.getDictId());
        assertNull(clone.getDictBasicInfo());
        assertNull(clone.getDictExtendInfo());
    }

    @Test
    void shouldCloneWithAllFields() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(new DictId(1L));
        domain.setDictBasicInfo(new DictBasicInfo("dictName", "dictCode"));
        domain.setDictExtendInfo(new DictExtendInfo(1, true));

        SysDictDomain clone = domain.clone();

        assertNotNull(clone);
        assertEquals(1L, clone.getDictId().value());
        assertEquals("dictName", clone.getDictBasicInfo().dictName());
        assertEquals(1, clone.getDictExtendInfo().sortOrder());
    }

    @Test
    void shouldHandleCloneNotSupportedException() {
        SysDictDomain domain = new SysDictDomain() {
            @Override
            protected Object doClone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("test");
            }
        };
        assertThrows(AssertionError.class, domain::clone);
    }
}
