package com.springddd.application.service.role.dto;

import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysRoleViewMapStruct {

    SysRoleView toView(SysRoleEntity entity);

    List<SysRoleView> toViewList(List<SysRoleEntity> entities);
}
