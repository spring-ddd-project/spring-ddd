package com.springddd.application.service.role.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.domain.role.DataPermission;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysRoleViewMapStructTest {

    private final SysRoleViewMapStruct mapper = new SysRoleViewMapStruct() {
        @Override
        public SysRoleView toView(SysRoleEntity entity) {
            return null;
        }

        @Override
        public java.util.List<SysRoleView> toViewList(java.util.List<SysRoleEntity> entities) {
            return null;
        }
    };

    @Test
    void toDataPermission_shouldReturnNull_whenInputIsNull() {
        DataPermission result = mapper.toDataPermission(null);
        assertNull(result);
    }

    @Test
    void toDataPermission_shouldReturnNull_whenInputIsEmpty() {
        DataPermission result = mapper.toDataPermission("");
        assertNull(result);
    }

    @Test
    void toDataPermission_shouldReturnObject_whenInputIsValidJson() throws JsonProcessingException {
        DataPermission permission = new DataPermission();
        String json = new ObjectMapper().writeValueAsString(permission);
        DataPermission result = mapper.toDataPermission(json);
        assertNotNull(result);
    }

    @Test
    void toDataPermission_shouldReturnNull_whenInputIsInvalidJson() {
        DataPermission result = mapper.toDataPermission("invalid json");
        assertNull(result);
    }
}
