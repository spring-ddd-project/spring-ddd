package com.springddd.infrastructure.persistence.factory.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.domain.dept.*;
import com.springddd.domain.dict.*;
import com.springddd.domain.gen.*;
import com.springddd.domain.menu.*;
import com.springddd.domain.role.*;
import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultEntityFactoryTest {

    private DefaultEntityFactory factory;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        factory = new DefaultEntityFactory(objectMapper);
    }

    // ==================== GenAggregate ====================

    @Test
    @DisplayName("createGenAggregateEntity 应将 domain 正确转换为 entity")
    void createGenAggregateEntity_shouldConvertCorrectly() {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setAggregateId(new AggregateId(1L));
        domain.setInfoId(new InfoId(2L));
        domain.setValueObject(new GenAggregateValueObject("name", "value", (byte) 1));
        domain.setExtendInfo(new GenAggregateExtendInfo(true));
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        GenAggregateEntity entity = factory.createGenAggregateEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getInfoId()).isEqualTo(2L);
        assertThat(entity.getObjectName()).isEqualTo("name");
        assertThat(entity.getObjectValue()).isEqualTo("value");
        assertThat(entity.getObjectType()).isEqualTo((byte) 1);
        assertThat(entity.getHasCreated()).isTrue();
        assertThat(entity.getCreateBy()).isEqualTo("admin");
        assertThat(entity.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("createGenAggregateDomain 应将 entity 正确转换为 domain")
    void createGenAggregateDomain_shouldConvertCorrectly() {
        GenAggregateEntity entity = new GenAggregateEntity();
        entity.setId(1L);
        entity.setInfoId(2L);
        entity.setObjectName("name");
        entity.setObjectValue("value");
        entity.setObjectType((byte) 1);
        entity.setHasCreated(true);
        entity.setCreateBy("admin");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setVersion(1);

        GenAggregateDomain domain = factory.createGenAggregateDomain(entity);

        assertThat(domain.getAggregateId().value()).isEqualTo(1L);
        assertThat(domain.getInfoId().value()).isEqualTo(2L);
        assertThat(domain.getValueObject().objectName()).isEqualTo("name");
        assertThat(domain.getValueObject().objectValue()).isEqualTo("value");
        assertThat(domain.getValueObject().objectType()).isEqualTo((byte) 1);
        assertThat(domain.getExtendInfo().hasCreated()).isTrue();
        assertThat(domain.getCreateBy()).isEqualTo("admin");
        assertThat(domain.getVersion()).isEqualTo(1);
    }

    // ==================== SysUser ====================

    @Test
    @DisplayName("createSysUserEntity 应将 domain 正确转换为 entity")
    void createSysUserEntity_shouldConvertCorrectly() {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(1L));
        domain.setAccount(new Account(new Username("admin"), new Password("123456"), "admin@test.com", "13800138000", false));
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.jpg");
        extendInfo.setSex(true);
        domain.setExtendInfo(extendInfo);
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setCreateBy("system");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("system");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        SysUserEntity entity = factory.createSysUserEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getUsername()).isEqualTo("admin");
        assertThat(entity.getPassword()).isEqualTo("123456");
        assertThat(entity.getEmail()).isEqualTo("admin@test.com");
        assertThat(entity.getPhone()).isEqualTo("13800138000");
        assertThat(entity.getLockStatus()).isFalse();
        assertThat(entity.getAvatar()).isEqualTo("avatar.jpg");
        assertThat(entity.getSex()).isTrue();
        assertThat(entity.getDeptId()).isEqualTo(1L);
        assertThat(entity.getDeleteStatus()).isFalse();
        assertThat(entity.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("createSysUserDomain 应将 entity 正确转换为 domain")
    void createSysUserDomain_shouldConvertCorrectly() {
        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setUsername("admin");
        entity.setPassword("123456");
        entity.setEmail("admin@test.com");
        entity.setPhone("13800138000");
        entity.setLockStatus(false);
        entity.setAvatar("avatar.jpg");
        entity.setSex(true);
        entity.setDeptId(1L);
        entity.setDeleteStatus(false);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setVersion(1);

        SysUserDomain domain = factory.createSysUserDomain(entity);

        assertThat(domain.getUserId().value()).isEqualTo(1L);
        assertThat(domain.getAccount().username().value()).isEqualTo("admin");
        assertThat(domain.getAccount().password().value()).isEqualTo("123456");
        assertThat(domain.getAccount().email()).isEqualTo("admin@test.com");
        assertThat(domain.getAccount().phone()).isEqualTo("13800138000");
        assertThat(domain.getAccount().lockStatus()).isFalse();
        assertThat(domain.getExtendInfo().getAvatar()).isEqualTo("avatar.jpg");
        assertThat(domain.getExtendInfo().getSex()).isTrue();
        assertThat(domain.getDeptId()).isEqualTo(1L);
        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("createSysUserEntity 当 account 为 null 时应处理为空")
    void createSysUserEntity_withNullAccount_shouldHandleGracefully() {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(1L));
        domain.setAccount(null);
        domain.setExtendInfo(null);

        SysUserEntity entity = factory.createSysUserEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getUsername()).isNull();
        assertThat(entity.getPassword()).isNull();
        assertThat(entity.getEmail()).isNull();
        assertThat(entity.getPhone()).isNull();
        assertThat(entity.getLockStatus()).isNull();
        assertThat(entity.getAvatar()).isNull();
        assertThat(entity.getSex()).isNull();
    }

    @Test
    @DisplayName("createSysUserEntity 当 username 和 password 为 null 时应处理为空")
    void createSysUserEntity_withNullUsernameAndPassword_shouldHandleGracefully() {
        Account mockAccount = mock(Account.class);
        when(mockAccount.username()).thenReturn(null);
        when(mockAccount.password()).thenReturn(null);
        when(mockAccount.phone()).thenReturn("13800138000");
        when(mockAccount.email()).thenReturn("test@test.com");
        when(mockAccount.lockStatus()).thenReturn(false);

        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(1L));
        domain.setAccount(mockAccount);
        domain.setExtendInfo(null);

        SysUserEntity entity = factory.createSysUserEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getUsername()).isNull();
        assertThat(entity.getPassword()).isNull();
        assertThat(entity.getPhone()).isEqualTo("13800138000");
        assertThat(entity.getEmail()).isEqualTo("test@test.com");
        assertThat(entity.getLockStatus()).isFalse();
    }

    // ==================== SysDept ====================

    @Test
    @DisplayName("createSysDeptEntity 应将 domain 正确转换为 entity")
    void createSysDeptEntity_shouldConvertCorrectly() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(new com.springddd.domain.dept.DeptId(1L));
        domain.setParentId(new com.springddd.domain.dept.DeptId(2L));
        domain.setDeptBasicInfo(new com.springddd.domain.dept.DeptBasicInfo("tech"));
        domain.setDeptExtendInfo(new com.springddd.domain.dept.DeptExtendInfo(1, true));
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        SysDeptEntity entity = factory.createSysDeptEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getParentId()).isEqualTo(2L);
        assertThat(entity.getDeptName()).isEqualTo("tech");
        assertThat(entity.getSortOrder()).isEqualTo(1);
        assertThat(entity.getDeptStatus()).isTrue();
        assertThat(entity.getDeleteStatus()).isFalse();
        assertThat(entity.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("createSysDeptDomain 应将 entity 正确转换为 domain")
    void createSysDeptDomain_shouldConvertCorrectly() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setParentId(2L);
        entity.setDeptName("tech");
        entity.setSortOrder(1);
        entity.setDeptStatus(true);
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setVersion(1);

        SysDeptDomain domain = factory.createSysDeptDomain(entity);

        assertThat(domain.getId().value()).isEqualTo(1L);
        assertThat(domain.getParentId().value()).isEqualTo(2L);
        assertThat(domain.getDeptBasicInfo().deptName()).isEqualTo("tech");
        assertThat(domain.getDeptExtendInfo().sortOrder()).isEqualTo(1);
        assertThat(domain.getDeptExtendInfo().deptStatus()).isTrue();
        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("createSysDeptEntity 当基本值为 null 时应处理为空")
    void createSysDeptEntity_withNullValues_shouldHandleGracefully() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(null);
        domain.setParentId(null);
        domain.setDeptBasicInfo(null);
        domain.setDeptExtendInfo(null);

        SysDeptEntity entity = factory.createSysDeptEntity(domain);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getParentId()).isNull();
        assertThat(entity.getDeptName()).isNull();
        assertThat(entity.getSortOrder()).isNull();
        assertThat(entity.getDeptStatus()).isNull();
    }

    // ==================== SysDict ====================

    @Test
    @DisplayName("createSysDictEntity 应将 domain 正确转换为 entity")
    void createSysDictEntity_shouldConvertCorrectly() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(new com.springddd.domain.dict.DictId(1L));
        domain.setDictBasicInfo(new com.springddd.domain.dict.DictBasicInfo("status", "sys_status"));
        domain.setDictExtendInfo(new com.springddd.domain.dict.DictExtendInfo(1, true));
        domain.setDeleteStatus(false);
        domain.setVersion(1);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());

        SysDictEntity entity = factory.createSysDictEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getDictName()).isEqualTo("status");
        assertThat(entity.getDictCode()).isEqualTo("sys_status");
        assertThat(entity.getSortOrder()).isEqualTo(1);
        assertThat(entity.getDictStatus()).isTrue();
        assertThat(entity.getDeleteStatus()).isFalse();
        assertThat(entity.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("createSysDictDomain 应将 entity 正确转换为 domain")
    void createSysDictDomain_shouldConvertCorrectly() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("status");
        entity.setDictCode("sys_status");
        entity.setSortOrder(1);
        entity.setDictStatus(true);
        entity.setDeleteStatus(false);
        entity.setVersion(1);
        entity.setCreateBy("admin");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(LocalDateTime.now());

        SysDictDomain domain = factory.createSysDictDomain(entity);

        assertThat(domain.getDictId().value()).isEqualTo(1L);
        assertThat(domain.getDictBasicInfo().dictName()).isEqualTo("status");
        assertThat(domain.getDictBasicInfo().dictCode()).isEqualTo("sys_status");
        assertThat(domain.getDictExtendInfo().sortOrder()).isEqualTo(1);
        assertThat(domain.getDictExtendInfo().dictStatus()).isTrue();
        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getVersion()).isEqualTo(1);
    }

    // ==================== SysDictItem ====================

    @Test
    @DisplayName("createSysDictItemEntity 应将 domain 正确转换为 entity")
    void createSysDictItemEntity_shouldConvertCorrectly() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(new com.springddd.domain.dict.DictItemId(1L));
        domain.setDictId(new com.springddd.domain.dict.DictId(2L));
        domain.setItemBasicInfo(new com.springddd.domain.dict.DictItemBasicInfo("enable", 1));
        domain.setItemExtendInfo(new com.springddd.domain.dict.DictItemExtendInfo(1, true));
        domain.setDeleteStatus(false);
        domain.setVersion(1);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());

        SysDictItemEntity entity = factory.createSysDictItemEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getDictId()).isEqualTo(2L);
        assertThat(entity.getItemLabel()).isEqualTo("enable");
        assertThat(entity.getItemValue()).isEqualTo(1);
        assertThat(entity.getSortOrder()).isEqualTo(1);
        assertThat(entity.getItemStatus()).isTrue();
    }

    @Test
    @DisplayName("createSysDictItemDomain 应将 entity 正确转换为 domain")
    void createSysDictItemDomain_shouldConvertCorrectly() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDictId(2L);
        entity.setItemLabel("enable");
        entity.setItemValue(1);
        entity.setSortOrder(1);
        entity.setItemStatus(true);
        entity.setDeleteStatus(false);
        entity.setVersion(1);
        entity.setCreateBy("admin");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(LocalDateTime.now());

        SysDictItemDomain domain = factory.createSysDictItemDomain(entity);

        assertThat(domain.getItemId().value()).isEqualTo(1L);
        assertThat(domain.getDictId().value()).isEqualTo(2L);
        assertThat(domain.getItemBasicInfo().itemLabel()).isEqualTo("enable");
        assertThat(domain.getItemBasicInfo().itemValue()).isEqualTo(1);
        assertThat(domain.getItemExtendInfo().sortOrder()).isEqualTo(1);
        assertThat(domain.getItemExtendInfo().itemStatus()).isTrue();
    }

    // ==================== SysMenu ====================

    @Test
    @DisplayName("createSysMenuEntity 应将 domain 正确转换为 entity")
    void createSysMenuEntity_shouldConvertCorrectly() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new com.springddd.domain.menu.MenuId(1L));
        domain.setParentId(new com.springddd.domain.menu.MenuId(2L));
        domain.setName("system");
        domain.setCatalog(new com.springddd.domain.menu.Catalog(null, null, "/system"));
        domain.setMenu(new com.springddd.domain.menu.Menu("/path", "component", false, false, false));
        domain.setButton(new com.springddd.domain.menu.Button("sys:user:add", "/api/user"));
        domain.setMenuExtendInfo(new com.springddd.domain.menu.MenuExtendInfo(1, "title", "icon", 1, true, true));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysMenuEntity entity = factory.createSysMenuEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getParentId()).isEqualTo(2L);
        assertThat(entity.getName()).isEqualTo("system");
        assertThat(entity.getRedirect()).isEqualTo("/system");
        assertThat(entity.getPath()).isEqualTo("/path");
        assertThat(entity.getComponent()).isEqualTo("component");
        assertThat(entity.getPermission()).isEqualTo("sys:user:add");
        assertThat(entity.getApi()).isEqualTo("/api/user");
        assertThat(entity.getSortOrder()).isEqualTo(1);
        assertThat(entity.getTitle()).isEqualTo("title");
        assertThat(entity.getIcon()).isEqualTo("icon");
        assertThat(entity.getMenuType()).isEqualTo(1);
        assertThat(entity.getVisible()).isTrue();
        assertThat(entity.getMenuStatus()).isTrue();
        assertThat(entity.getDeptId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("createSysMenuDomain 应将 entity 正确转换为 domain")
    void createSysMenuDomain_shouldConvertCorrectly() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setParentId(2L);
        entity.setName("system");
        entity.setRedirect("/system");
        entity.setPath("/path");
        entity.setComponent("component");
        entity.setAffixTab(false);
        entity.setNoBasicLayout(false);
        entity.setEmbedded(false);
        entity.setPermission("sys:user:add");
        entity.setApi("/api/user");
        entity.setSortOrder(1);
        entity.setTitle("title");
        entity.setIcon("icon");
        entity.setMenuType(1);
        entity.setVisible(true);
        entity.setMenuStatus(true);
        entity.setDeptId(1L);
        entity.setDeleteStatus(false);
        entity.setVersion(1);

        SysMenuDomain domain = factory.createSysMenuDomain(entity);

        assertThat(domain.getMenuId().value()).isEqualTo(1L);
        assertThat(domain.getParentId().value()).isEqualTo(2L);
        assertThat(domain.getName()).isEqualTo("system");
        assertThat(domain.getCatalog().redirect()).isEqualTo("/system");
        assertThat(domain.getMenu().menuPath()).isEqualTo("/path");
        assertThat(domain.getButton().permission()).isEqualTo("sys:user:add");
        assertThat(domain.getMenuExtendInfo().order()).isEqualTo(1);
        assertThat(domain.getMenuExtendInfo().title()).isEqualTo("title");
        assertThat(domain.getMenuExtendInfo().menuType()).isEqualTo(1);
        assertThat(domain.getDeptId()).isEqualTo(1L);
    }

    // ==================== SysRole ====================

    @Test
    @DisplayName("createSysRoleEntity 应将 domain 正确转换为 entity")
    void createSysRoleEntity_shouldConvertCorrectly() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new com.springddd.domain.role.RoleId(1L));
        domain.setRoleBasicInfo(new com.springddd.domain.role.RoleBasicInfo("admin", "ROLE_ADMIN", true));
        domain.setRoleExtendInfo(new com.springddd.domain.role.RoleExtendInfo("desc", true));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysRoleEntity entity = factory.createSysRoleEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getRoleName()).isEqualTo("admin");
        assertThat(entity.getRoleCode()).isEqualTo("ROLE_ADMIN");
        assertThat(entity.getOwnerStatus()).isTrue();
        assertThat(entity.getRoleDesc()).isEqualTo("desc");
        assertThat(entity.getRoleStatus()).isTrue();
        assertThat(entity.getDeptId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("createSysRoleDomain 应将 entity 正确转换为 domain")
    void createSysRoleDomain_shouldConvertCorrectly() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("admin");
        entity.setRoleCode("ROLE_ADMIN");
        entity.setOwnerStatus(true);
        entity.setRoleDesc("desc");
        entity.setRoleStatus(true);
        entity.setDeptId(1L);
        entity.setDeleteStatus(false);
        entity.setVersion(1);

        SysRoleDomain domain = factory.createSysRoleDomain(entity);

        assertThat(domain.getRoleId().value()).isEqualTo(1L);
        assertThat(domain.getRoleBasicInfo().roleName()).isEqualTo("admin");
        assertThat(domain.getRoleBasicInfo().roleCode()).isEqualTo("ROLE_ADMIN");
        assertThat(domain.getRoleBasicInfo().roleOwner()).isTrue();
        assertThat(domain.getRoleExtendInfo().roleDesc()).isEqualTo("desc");
        assertThat(domain.getRoleExtendInfo().roleStatus()).isTrue();
        assertThat(domain.getDeptId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("createSysRoleEntity 当基本值为 null 时应处理为空")
    void createSysRoleEntity_withNullValues_shouldHandleGracefully() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new com.springddd.domain.role.RoleId(1L));
        domain.setRoleBasicInfo(null);
        domain.setRoleExtendInfo(null);
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysRoleEntity entity = factory.createSysRoleEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getRoleName()).isNull();
        assertThat(entity.getRoleCode()).isNull();
        assertThat(entity.getOwnerStatus()).isNull();
        assertThat(entity.getRoleDesc()).isNull();
        assertThat(entity.getRoleStatus()).isNull();
    }

    // ==================== GenColumnBind ====================

    @Test
    @DisplayName("createGenColumnBindEntity 应将 domain 正确转换为 entity")
    void createGenColumnBindEntity_shouldConvertCorrectly() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setBindId(new com.springddd.domain.gen.ColumnBindId(1L));
        domain.setBasicInfo(new com.springddd.domain.gen.GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 2));
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenColumnBindEntity entity = factory.createGenColumnBindEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getColumnType()).isEqualTo("varchar");
        assertThat(entity.getEntityType()).isEqualTo("String");
        assertThat(entity.getComponentType()).isEqualTo((byte) 1);
        assertThat(entity.getTypescriptType()).isEqualTo((byte) 2);
    }

    @Test
    @DisplayName("createGenColumnBindDomain 应将 entity 正确转换为 domain")
    void createGenColumnBindDomain_shouldConvertCorrectly() {
        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(1L);
        entity.setColumnType("varchar");
        entity.setEntityType("String");
        entity.setComponentType((byte) 1);
        entity.setTypescriptType((byte) 2);
        entity.setDeleteStatus(false);
        entity.setVersion(1);

        GenColumnBindDomain domain = factory.createGenColumnBindDomain(entity);

        assertThat(domain.getBindId().value()).isEqualTo(1L);
        assertThat(domain.getBasicInfo().columnType()).isEqualTo("varchar");
        assertThat(domain.getBasicInfo().entityType()).isEqualTo("String");
        assertThat(domain.getBasicInfo().componentType()).isEqualTo((byte) 1);
        assertThat(domain.getBasicInfo().typescriptType()).isEqualTo((byte) 2);
    }

    // ==================== GenColumns ====================

    @Test
    @DisplayName("createGenColumnsEntity 应将 domain 正确转换为 entity")
    void createGenColumnsEntity_shouldConvertCorrectly() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(new com.springddd.domain.gen.ColumnsId(1L));
        domain.setInfoId(new com.springddd.domain.gen.InfoId(2L));
        domain.setProp(new com.springddd.domain.gen.Prop("id", "id", "bigint", "主键", "Long", "Id"));
        domain.setTable(new com.springddd.domain.gen.Table(true, true, true, (byte) 1, (byte) 2));
        domain.setForm(new com.springddd.domain.gen.Form((byte) 1, true, true));
        domain.setI18n(new com.springddd.domain.gen.I18n("en", "zh"));
        domain.setExtendInfo(new com.springddd.domain.gen.GenColumnsExtendInfo(1L, (byte) 1));
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenColumnsEntity entity = factory.createGenColumnsEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getInfoId()).isEqualTo(2L);
        assertThat(entity.getPropColumnKey()).isEqualTo("id");
        assertThat(entity.getPropColumnName()).isEqualTo("id");
        assertThat(entity.getPropColumnType()).isEqualTo("bigint");
        assertThat(entity.getTableVisible()).isTrue();
        assertThat(entity.getFormComponent()).isEqualTo((byte) 1);
        assertThat(entity.getEn()).isEqualTo("en");
        assertThat(entity.getPropDictId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("createGenColumnsDomain 应将 entity 正确转换为 domain")
    void createGenColumnsDomain_shouldConvertCorrectly() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setId(1L);
        entity.setInfoId(2L);
        entity.setPropColumnKey("id");
        entity.setPropColumnName("id");
        entity.setPropColumnType("bigint");
        entity.setPropColumnComment("主键");
        entity.setPropJavaType("Long");
        entity.setPropJavaEntity("Id");
        entity.setTableVisible(true);
        entity.setTableOrder(true);
        entity.setTableFilter(true);
        entity.setTableFilterComponent((byte) 1);
        entity.setTableFilterType((byte) 2);
        entity.setFormComponent((byte) 1);
        entity.setFormVisible(true);
        entity.setFormRequired(true);
        entity.setEn("en");
        entity.setLocale("zh");
        entity.setPropDictId(1L);
        entity.setTypescriptType((byte) 1);
        entity.setDeleteStatus(false);
        entity.setVersion(1);

        GenColumnsDomain domain = factory.createGenColumnsDomain(entity);

        assertThat(domain.getId().value()).isEqualTo(1L);
        assertThat(domain.getInfoId().value()).isEqualTo(2L);
        assertThat(domain.getProp().propColumnKey()).isEqualTo("id");
        assertThat(domain.getProp().propColumnName()).isEqualTo("id");
        assertThat(domain.getTable().tableVisible()).isTrue();
        assertThat(domain.getForm().formComponent()).isEqualTo((byte) 1);
        assertThat(domain.getI18n().en()).isEqualTo("en");
        assertThat(domain.getExtendInfo().propDictId()).isEqualTo(1L);
    }

    // ==================== GenProjectInfo ====================

    @Test
    @DisplayName("createGenProjectInfoEntity 应将 domain 正确转换为 entity")
    void createGenProjectInfoEntity_shouldConvertCorrectly() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(new com.springddd.domain.gen.InfoId(1L));
        domain.setProjectInfo(new com.springddd.domain.gen.ProjectInfo("sys_user", "com.example", "SysUser", "system", "demo"));
        domain.setExtendInfo(new com.springddd.domain.gen.GenProjectInfoExtendInfo("request", null));
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenProjectInfoEntity entity = factory.createGenProjectInfoEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getTableName()).isEqualTo("sys_user");
        assertThat(entity.getPackageName()).isEqualTo("com.example");
        assertThat(entity.getClassName()).isEqualTo("SysUser");
        assertThat(entity.getModuleName()).isEqualTo("system");
        assertThat(entity.getProjectName()).isEqualTo("demo");
        assertThat(entity.getRequestName()).isEqualTo("request");
    }

    @Test
    @DisplayName("createGenProjectInfoDomain 应将 entity 正确转换为 domain")
    void createGenProjectInfoDomain_shouldConvertCorrectly() {
        GenProjectInfoEntity entity = new GenProjectInfoEntity();
        entity.setId(1L);
        entity.setTableName("sys_user");
        entity.setPackageName("com.example");
        entity.setClassName("SysUser");
        entity.setModuleName("system");
        entity.setProjectName("demo");
        entity.setRequestName("request");
        entity.setDeleteStatus(false);
        entity.setVersion(1);

        GenProjectInfoDomain domain = factory.createGenProjectInfoDomain(entity);

        assertThat(domain.getId().value()).isEqualTo(1L);
        assertThat(domain.getProjectInfo().tableName()).isEqualTo("sys_user");
        assertThat(domain.getProjectInfo().packageName()).isEqualTo("com.example");
        assertThat(domain.getProjectInfo().className()).isEqualTo("SysUser");
        assertThat(domain.getProjectInfo().moduleName()).isEqualTo("system");
        assertThat(domain.getProjectInfo().projectName()).isEqualTo("demo");
        assertThat(domain.getExtendInfo().requestName()).isEqualTo("request");
    }

    // ==================== GenTemplate ====================

    @Test
    @DisplayName("createGenTemplateEntity 应将 domain 正确转换为 entity")
    void createGenTemplateEntity_shouldConvertCorrectly() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setId(new com.springddd.domain.gen.TemplateId(1L));
        domain.setTemplateInfo(new com.springddd.domain.gen.TemplateInfo("name", "content"));
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenTemplateEntity entity = factory.createGenTemplateEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getTemplateName()).isEqualTo("name");
        assertThat(entity.getTemplateContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("createGenTemplateDomain 应将 entity 正确转换为 domain")
    void createGenTemplateDomain_shouldConvertCorrectly() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        entity.setTemplateName("name");
        entity.setTemplateContent("content");
        entity.setDeleteStatus(false);
        entity.setVersion(1);

        GenTemplateDomain domain = factory.createGenTemplateDomain(entity);

        assertThat(domain.getId().value()).isEqualTo(1L);
        assertThat(domain.getTemplateInfo().templateName()).isEqualTo("name");
        assertThat(domain.getTemplateInfo().templateContent()).isEqualTo("content");
    }

    // ==================== Null Branch Coverage ====================

    @Test
    @DisplayName("createSysMenuEntity 当所有可选值为 null 时应处理为空")
    void createSysMenuEntity_withAllNullValues_shouldHandleGracefully() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new com.springddd.domain.menu.MenuId(1L));
        domain.setParentId(new com.springddd.domain.menu.MenuId(2L));
        domain.setName("menu");
        domain.setCatalog(null);
        domain.setMenu(null);
        domain.setButton(null);
        domain.setMenuExtendInfo(null);
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysMenuEntity entity = factory.createSysMenuEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getRedirect()).isNull();
        assertThat(entity.getPath()).isNull();
        assertThat(entity.getComponent()).isNull();
        assertThat(entity.getPermission()).isNull();
        assertThat(entity.getApi()).isNull();
        assertThat(entity.getSortOrder()).isNull();
        assertThat(entity.getTitle()).isNull();
        assertThat(entity.getIcon()).isNull();
        assertThat(entity.getMenuType()).isNull();
        assertThat(entity.getVisible()).isNull();
        assertThat(entity.getMenuStatus()).isNull();
    }

    @Test
    @DisplayName("createSysDictEntity 当所有可选值为 null 时应处理为空")
    void createSysDictEntity_withAllNullValues_shouldHandleGracefully() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(new com.springddd.domain.dict.DictId(1L));
        domain.setDictBasicInfo(null);
        domain.setDictExtendInfo(null);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysDictEntity entity = factory.createSysDictEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getDictName()).isNull();
        assertThat(entity.getDictCode()).isNull();
        assertThat(entity.getSortOrder()).isNull();
        assertThat(entity.getDictStatus()).isNull();
    }

    @Test
    @DisplayName("createSysDictItemEntity 当所有可选值为 null 时应处理为空")
    void createSysDictItemEntity_withAllNullValues_shouldHandleGracefully() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(new com.springddd.domain.dict.DictItemId(1L));
        domain.setDictId(null);
        domain.setItemBasicInfo(null);
        domain.setItemExtendInfo(null);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysDictItemEntity entity = factory.createSysDictItemEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getDictId()).isNull();
        assertThat(entity.getItemLabel()).isNull();
        assertThat(entity.getItemValue()).isNull();
        assertThat(entity.getSortOrder()).isNull();
        assertThat(entity.getItemStatus()).isNull();
    }

    @Test
    @DisplayName("createGenColumnBindEntity 当所有可选值为 null 时应处理为空")
    void createGenColumnBindEntity_withAllNullValues_shouldHandleGracefully() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setBindId(null);
        domain.setBasicInfo(null);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenColumnBindEntity entity = factory.createGenColumnBindEntity(domain);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getColumnType()).isNull();
        assertThat(entity.getEntityType()).isNull();
        assertThat(entity.getComponentType()).isNull();
        assertThat(entity.getTypescriptType()).isNull();
    }

    @Test
    @DisplayName("createGenColumnsEntity 当所有可选值为 null 时应处理为空")
    void createGenColumnsEntity_withAllNullValues_shouldHandleGracefully() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(new com.springddd.domain.gen.ColumnsId(1L));
        domain.setInfoId(null);
        domain.setProp(null);
        domain.setTable(null);
        domain.setForm(null);
        domain.setI18n(null);
        domain.setExtendInfo(null);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenColumnsEntity entity = factory.createGenColumnsEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getInfoId()).isNull();
        assertThat(entity.getPropColumnKey()).isNull();
        assertThat(entity.getTableVisible()).isNull();
        assertThat(entity.getFormComponent()).isNull();
        assertThat(entity.getEn()).isNull();
        assertThat(entity.getPropDictId()).isNull();
        assertThat(entity.getTypescriptType()).isNull();
    }

    @Test
    @DisplayName("createGenProjectInfoEntity 当所有可选值为 null 时应处理为空")
    void createGenProjectInfoEntity_withAllNullValues_shouldHandleGracefully() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(new com.springddd.domain.gen.InfoId(1L));
        domain.setProjectInfo(null);
        domain.setExtendInfo(null);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenProjectInfoEntity entity = factory.createGenProjectInfoEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getTableName()).isNull();
        assertThat(entity.getPackageName()).isNull();
        assertThat(entity.getClassName()).isNull();
        assertThat(entity.getModuleName()).isNull();
        assertThat(entity.getProjectName()).isNull();
        assertThat(entity.getRequestName()).isNull();
    }

    @Test
    @DisplayName("createGenTemplateEntity 当 templateInfo 为 null 时应处理为空")
    void createGenTemplateEntity_withNullTemplateInfo_shouldHandleGracefully() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setId(new com.springddd.domain.gen.TemplateId(1L));
        domain.setTemplateInfo(null);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenTemplateEntity entity = factory.createGenTemplateEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getTemplateName()).isNull();
        assertThat(entity.getTemplateContent()).isNull();
    }
}
