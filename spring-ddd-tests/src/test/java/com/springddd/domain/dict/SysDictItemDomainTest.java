package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDictItemDomainTest {

    @Test
    void shouldCreateSysDictItemDomain() {
        SysDictItemDomain domain = new SysDictItemDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetId() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictItemId id = new DictItemId(1L);
        domain.setItemId(id);
        assertEquals(id, domain.getItemId());
    }

    @Test
    void shouldSetAndGetDictId() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictId dictId = new DictId(1L);
        domain.setDictId(dictId);
        assertEquals(dictId, domain.getDictId());
    }

    @Test
    void shouldSetAndGetBasicInfo() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("label", 1);
        domain.setItemBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getItemBasicInfo());
    }

    @Test
    void shouldSetAndGetExtendInfo() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(1, true);
        domain.setItemExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getItemExtendInfo());
    }

    @Test
    void shouldCallCreate() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void shouldUpdateSysDictItemDomain() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("label", 1);
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(1, true);

        domain.update(basicInfo, extendInfo);

        assertEquals(basicInfo, domain.getItemBasicInfo());
        assertEquals(extendInfo, domain.getItemExtendInfo());
    }

    @Test
    void shouldSetDeleteStatusOnDelete() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldClearDeleteStatusOnRestore() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SysDictItemDomain domain = new SysDictItemDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysDictItemDomain"));
    }

    @Test
    void shouldEnableWhenStateIsNullAndItemStatusTrue() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));

        domain.enable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldEnableWhenStateIsNullAndItemStatusFalse() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, false));

        domain.enable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldEnableWhenStateIsNullAndItemBasicInfoIsNull() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(null);

        domain.enable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldDisableWhenStateIsNull() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));

        domain.disable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldRestoreWhenStateIsNullAndItemStatusTrue() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));
        domain.setDeleteStatus(true);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldRestoreWhenStateIsNullAndItemStatusFalse() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, false));
        domain.setDeleteStatus(true);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldRestoreWhenStateExists() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));
        domain.setState(new com.springddd.domain.dict.state.EnabledDictItemState());
        domain.setDeleteStatus(true);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCloneWithNullFields() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(null);
        domain.setDictId(null);
        domain.setItemBasicInfo(null);
        domain.setItemExtendInfo(null);

        SysDictItemDomain clone = domain.clone();

        assertNotNull(clone);
        assertNull(clone.getItemId());
        assertNull(clone.getDictId());
        assertNull(clone.getItemBasicInfo());
        assertNull(clone.getItemExtendInfo());
    }

    @Test
    void shouldCloneWithAllFields() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(new DictItemId(1L));
        domain.setDictId(new DictId(1L));
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));
        domain.setItemExtendInfo(new DictItemExtendInfo(1, true));

        SysDictItemDomain clone = domain.clone();

        assertNotNull(clone);
        assertEquals(1L, clone.getItemId().value());
        assertEquals(1L, clone.getDictId().value());
        assertEquals("label", clone.getItemBasicInfo().itemLabel());
        assertEquals(1, clone.getItemExtendInfo().sortOrder());
    }

    @Test
    void shouldHandleCloneNotSupportedException() {
        SysDictItemDomain domain = new SysDictItemDomain() {
            @Override
            protected Object doClone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("test");
            }
        };
        assertThrows(AssertionError.class, domain::clone);
    }

    @Test
    void shouldDisableWhenStateIsNullAndItemBasicInfoIsNull() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(null);

        domain.disable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldDisableWhenStateIsNullAndItemStatusFalse() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, false));

        domain.disable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldDisableWhenStateExists() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));
        domain.setState(new com.springddd.domain.dict.state.DisabledDictItemState());

        domain.disable();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldEnableWhenStateExists() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));
        domain.setState(new com.springddd.domain.dict.state.DisabledDictItemState());

        domain.enable();

        assertTrue(domain.getState() instanceof com.springddd.domain.dict.state.EnabledDictItemState);
    }

    @Test
    void shouldRestoreWhenStateIsNullAndItemBasicInfoIsNull() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(null);
        domain.setDeleteStatus(true);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }
}
