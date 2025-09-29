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
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DefaultEntityFactory implements EntityFactory {

    private final ObjectMapper objectMapper;

    @Override
    public GenAggregateEntity createGenAggregateEntity(GenAggregateDomain domain) {
        GenAggregateEntity entity = new GenAggregateEntity();
        entity.setId(Optional.ofNullable(domain.getAggregateId()).map(AggregateId::value).orElse(null));
        entity.setInfoId(domain.getInfoId().value());
        entity.setObjectName(domain.getValueObject().objectName());
        entity.setObjectValue(domain.getValueObject().objectValue());
        entity.setObjectType(domain.getValueObject().objectType());
        entity.setHasCreated(domain.getExtendInfo().hasCreated());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public GenAggregateDomain createGenAggregateDomain(GenAggregateEntity entity) {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setAggregateId(new AggregateId(entity.getId()));
        domain.setInfoId(new InfoId(entity.getInfoId()));
        domain.setValueObject(new GenAggregateValueObject(entity.getObjectName(), entity.getObjectValue(), entity.getObjectType()));
        domain.setExtendInfo(new GenAggregateExtendInfo(entity.getHasCreated()));
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    }

    @Override
    public SysUserEntity createSysUserEntity(SysUserDomain domain) {
        SysUserEntity entity = new SysUserEntity();
        entity.setId(Optional.ofNullable(domain.getUserId()).map(UserId::value).orElse(null));
        
        if (domain.getAccount() != null) {
            entity.setUserName(domain.getAccount().getUsername() != null ? domain.getAccount().getUsername().value() : null);
            entity.setPassword(domain.getAccount().getPassword() != null ? domain.getAccount().getPassword().value() : null);
            entity.setPhone(domain.getAccount().getPhone());
            entity.setEmail(domain.getAccount().getEmail());
            entity.setLockStatus(domain.getAccount().getLockStatus());
        }
        
        if (domain.getExtendInfo() != null) {
            entity.setAvatar(domain.getExtendInfo().getAvatar());
            entity.setSex(domain.getExtendInfo().getSex());
        }

        entity.setDeptId(domain.getDeptId());
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public SysUserDomain createSysUserDomain(SysUserEntity entity) {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(entity.getId()));
        
        Account account = new Account();
        account.setUsername(new Username(entity.getUserName()));
        account.setPassword(new Password(entity.getPassword()));
        account.setPhone(entity.getPhone());
        account.setEmail(entity.getEmail());
        account.setLockStatus(entity.getLockStatus());
        domain.setAccount(account);
        
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar(entity.getAvatar());
        extendInfo.setSex(entity.getSex());
        domain.setExtendInfo(extendInfo);
        
        domain.setDeptId(entity.getDeptId());
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    }

    @Override
    public SysDeptEntity createSysDeptEntity(SysDeptDomain domain) {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(Optional.ofNullable(domain.getId()).map(DeptId::value).orElse(null));
        entity.setParentId(Optional.ofNullable(domain.getParentId()).map(DeptId::value).orElse(null));
        if (domain.getDeptBasicInfo() != null) {
            entity.setDeptName(domain.getDeptBasicInfo().deptName());
        }
        if (domain.getDeptExtendInfo() != null) {
            entity.setSortOrder(domain.getDeptExtendInfo().sortOrder());
            entity.setDeptStatus(domain.getDeptExtendInfo().deptStatus());
        }
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public SysDeptDomain createSysDeptDomain(SysDeptEntity entity) {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(new DeptId(entity.getId()));
        domain.setParentId(new DeptId(entity.getParentId()));
        domain.setDeptBasicInfo(new DeptBasicInfo(entity.getDeptName()));
        domain.setDeptExtendInfo(new DeptExtendInfo(entity.getSortOrder(), entity.getDeptStatus()));
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    }

    @Override
    public SysDictEntity createSysDictEntity(SysDictDomain domain) {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(Optional.ofNullable(domain.getDictId()).map(DictId::value).orElse(null));
        if (domain.getDictBasicInfo() != null) {
            entity.setDictName(domain.getDictBasicInfo().dictName());
            entity.setDictCode(domain.getDictBasicInfo().dictCode());
        }
        if (domain.getDictExtendInfo() != null) {
            entity.setSortOrder(domain.getDictExtendInfo().sortOrder());
            entity.setDictStatus(domain.getDictExtendInfo().dictStatus());
        }
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setVersion(domain.getVersion());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        return entity;
    }

    @Override
    public SysDictDomain createSysDictDomain(SysDictEntity entity) {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(new DictId(entity.getId()));
        domain.setDictBasicInfo(new DictBasicInfo(entity.getDictName(), entity.getDictCode()));
        domain.setDictExtendInfo(new DictExtendInfo(entity.getSortOrder(), entity.getDictStatus()));
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setVersion(entity.getVersion());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        return domain;
    }

    @Override
    public SysMenuEntity createSysMenuEntity(SysMenuDomain domain) {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(Optional.ofNullable(domain.getMenuId()).map(MenuId::value).orElse(null));
        entity.setParentId(Optional.ofNullable(domain.getParentId()).map(MenuId::value).orElse(null));
        entity.setName(domain.getName());
        
        if (domain.getCatalog() != null) {
            entity.setRedirect(domain.getCatalog().menuRedirect());
        }
        
        if (domain.getMenu() != null) {
            entity.setPath(domain.getMenu().menuPath());
            entity.setComponent(domain.getMenu().component());
            entity.setAffixTab(domain.getMenu().affixTab());
            entity.setNoBasicLayout(domain.getMenu().noBasicLayout());
            entity.setEmbedded(domain.getMenu().embedded());
        }
        
        if (domain.getButton() != null) {
            entity.setPermission(domain.getButton().permission());
            entity.setApi(domain.getButton().api());
        }
        
        if (domain.getMenuExtendInfo() != null) {
            entity.setSortOrder(domain.getMenuExtendInfo().order());
            entity.setTitle(domain.getMenuExtendInfo().title());
            entity.setIcon(domain.getMenuExtendInfo().icon());
            entity.setMenuType(domain.getMenuExtendInfo().menuType());
            entity.setVisible(domain.getMenuExtendInfo().visible());
            entity.setMenuStatus(domain.getMenuExtendInfo().menuStatus());
        }

        entity.setDeptId(domain.getDeptId());
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public SysMenuDomain createSysMenuDomain(SysMenuEntity entity) {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(entity.getId()));
        domain.setParentId(new MenuId(entity.getParentId()));
        domain.setName(entity.getName());
        domain.setCatalog(new Catalog(entity.getRedirect()));
        domain.setMenu(new Menu(entity.getPath(), entity.getComponent(), entity.getAffixTab(), entity.getNoBasicLayout(), entity.getEmbedded()));
        domain.setButton(new Button(entity.getPermission(), entity.getApi()));
        domain.setMenuExtendInfo(new MenuExtendInfo(entity.getSortOrder(), entity.getTitle(), entity.getIcon(), entity.getMenuType(), entity.getVisible(), entity.getMenuStatus()));
        
        domain.setDeptId(entity.getDeptId());
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    }

    @Override
    public SysRoleEntity createSysRoleEntity(SysRoleDomain domain) {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(Optional.ofNullable(domain.getRoleId()).map(RoleId::value).orElse(null));
        
        if (domain.getRoleBasicInfo() != null) {
            entity.setRoleName(domain.getRoleBasicInfo().roleName());
            entity.setRoleCode(domain.getRoleBasicInfo().roleCode());
            entity.setDataScope(domain.getRoleBasicInfo().roleDataScope());
            entity.setOwnerStatus(domain.getRoleBasicInfo().roleOwner());
        }
        
        if (domain.getRoleExtendInfo() != null) {
            entity.setRoleDesc(domain.getRoleExtendInfo().roleDesc());
            entity.setRoleStatus(domain.getRoleExtendInfo().roleStatus());
        }

        if (domain.getDataPermission() != null) {
            try {
                entity.setDataPermission(objectMapper.writeValueAsString(domain.getDataPermission()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Mapping error", e);
            }
        }

        entity.setDeptId(domain.getDeptId());
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public SysDictItemEntity createSysDictItemEntity(SysDictItemDomain domain) {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(Optional.ofNullable(domain.getItemId()).map(DictItemId::value).orElse(null));
        if (domain.getDictId() != null) {
            entity.setDictId(domain.getDictId().value());
        }
        if (domain.getItemBasicInfo() != null) {
            entity.setItemLabel(domain.getItemBasicInfo().itemLabel());
            entity.setItemValue(domain.getItemBasicInfo().itemValue());
        }
        if (domain.getItemExtendInfo() != null) {
            entity.setSortOrder(domain.getItemExtendInfo().sortOrder());
            entity.setItemStatus(domain.getItemExtendInfo().itemStatus());
        }
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public SysDictItemDomain createSysDictItemDomain(SysDictItemEntity entity) {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(new DictItemId(entity.getId()));
        domain.setDictId(new DictId(entity.getDictId()));
        domain.setItemBasicInfo(new DictItemBasicInfo(entity.getItemLabel(), entity.getItemValue()));
        domain.setItemExtendInfo(new DictItemExtendInfo(entity.getSortOrder(), entity.getItemStatus()));
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    }

    @Override
    public GenColumnBindEntity createGenColumnBindEntity(GenColumnBindDomain domain) {
        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(Optional.ofNullable(domain.getBindId()).map(ColumnBindId::value).orElse(null));
        if (domain.getBasicInfo() != null) {
            entity.setColumnType(domain.getBasicInfo().columnType());
            entity.setEntityType(domain.getBasicInfo().entityType());
            entity.setComponentType(domain.getBasicInfo().componentType());
            entity.setTypescriptType(domain.getBasicInfo().typescriptType());
        }
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public GenColumnBindDomain createGenColumnBindDomain(GenColumnBindEntity entity) {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setBindId(new ColumnBindId(entity.getId()));
        domain.setBasicInfo(new GenColumnBindBasicInfo(entity.getColumnType(), entity.getEntityType(), entity.getComponentType(), entity.getTypescriptType()));
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    }

    @Override
    public GenColumnsEntity createGenColumnsEntity(GenColumnsDomain domain) {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setId(Optional.ofNullable(domain.getId()).map(ColumnsId::value).orElse(null));
        if (domain.getInfoId() != null) {
            entity.setInfoId(domain.getInfoId().value());
        }
        if (domain.getProp() != null) {
            entity.setPropColumnKey(domain.getProp().propColumnKey());
            entity.setPropColumnName(domain.getProp().propColumnName());
            entity.setPropColumnType(domain.getProp().propColumnType());
            entity.setPropColumnComment(domain.getProp().propColumnComment());
            entity.setPropJavaType(domain.getProp().propJavaType());
            entity.setPropJavaEntity(domain.getProp().propJavaEntity());
        }
        if (domain.getTable() != null) {
            entity.setTableVisible(domain.getTable().tableVisible());
            entity.setTableFilter(domain.getTable().tableFilter());
            entity.setTableOrder(domain.getTable().tableOrder());
            entity.setTableFilterComponent(domain.getTable().tableFilterComponent());
            entity.setTableFilterType(domain.getTable().tableFilterType());
        }
        if (domain.getForm() != null) {
            entity.setFormComponent(domain.getForm().formComponent());
            entity.setFormVisible(domain.getForm().formVisible());
            entity.setFormRequired(domain.getForm().formRequired());
        }
        if (domain.getI18n() != null) {
            entity.setEn(domain.getI18n().en());
            entity.setLocale(domain.getI18n().locale());
        }
        if (domain.getExtendInfo() != null) {
            entity.setPropDictId(domain.getExtendInfo().propDictId());
            entity.setTypescriptType(domain.getExtendInfo().typescriptType());
        }
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public GenColumnsDomain createGenColumnsDomain(GenColumnsEntity entity) {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(new ColumnsId(entity.getId()));
        domain.setInfoId(new InfoId(entity.getInfoId()));
        domain.setProp(new Prop(entity.getPropColumnKey(), entity.getPropColumnName(), entity.getPropColumnType(), entity.getPropColumnComment(), entity.getPropJavaEntity(), entity.getPropJavaType()));
        domain.setTable(new Table(entity.getTableVisible(), entity.getTableOrder(), entity.getTableFilter(), entity.getTableFilterComponent(), entity.getTableFilterType()));
        domain.setForm(new Form(entity.getFormComponent(), entity.getFormVisible(), entity.getFormRequired()));
        domain.setI18n(new I18n(entity.getEn(), entity.getLocale()));
        domain.setExtendInfo(new GenColumnsExtendInfo(entity.getPropDictId(), entity.getTypescriptType()));
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    }

    @Override
    public GenProjectInfoEntity createGenProjectInfoEntity(GenProjectInfoDomain domain) {
        GenProjectInfoEntity entity = new GenProjectInfoEntity();
        entity.setId(Optional.ofNullable(domain.getId()).map(InfoId::value).orElse(null));
        if (domain.getProjectInfo() != null) {
            entity.setTableName(domain.getProjectInfo().tableName());
            entity.setPackageName(domain.getProjectInfo().packageName());
            entity.setClassName(domain.getProjectInfo().className());
            entity.setModuleName(domain.getProjectInfo().moduleName());
            entity.setProjectName(domain.getProjectInfo().projectName());
        }
        if (domain.getExtendInfo() != null) {
            entity.setRequestName(domain.getExtendInfo().requestName());
        }
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public GenProjectInfoDomain createGenProjectInfoDomain(GenProjectInfoEntity entity) {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(new InfoId(entity.getId()));
        domain.setProjectInfo(new ProjectInfo(entity.getTableName(), entity.getPackageName(), entity.getClassName(), entity.getModuleName(), entity.getProjectName()));
        domain.setExtendInfo(new GenProjectInfoExtendInfo(entity.getRequestName()));
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    }

    @Override
    public GenTemplateEntity createGenTemplateEntity(GenTemplateDomain domain) {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(Optional.ofNullable(domain.getId()).map(TemplateId::value).orElse(null));
        if (domain.getTemplateInfo() != null) {
            entity.setTemplateName(domain.getTemplateInfo().templateName());
            entity.setTemplateContent(domain.getTemplateInfo().templateContent());
        }
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public GenTemplateDomain createGenTemplateDomain(GenTemplateEntity entity) {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setId(new TemplateId(entity.getId()));
        domain.setTemplateInfo(new TemplateInfo(entity.getTemplateName(), entity.getTemplateContent()));
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    }
}
