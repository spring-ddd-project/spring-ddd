package com.springddd.application.service.role.dto;

import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysRoleMenuViewMapStruct {

    SysRoleMenuView toView(SysRoleMenuEntity entity);

    List<SysRoleMenuView> toViewList(List<SysRoleMenuEntity> entityList);
}
