package com.springddd.application.service.role.dto;

import com.springddd.infrastructure.persistence.entity.SysRoleMenuDataScopeEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysRoleMenuDataScopeViewMapStruct {

    SysRoleMenuDataScopeView toView(SysRoleMenuDataScopeEntity entity);

    List<SysRoleMenuDataScopeView> toViews(List<SysRoleMenuDataScopeEntity> entities);
}
