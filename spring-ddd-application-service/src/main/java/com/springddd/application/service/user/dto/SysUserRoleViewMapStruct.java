package com.springddd.application.service.user.dto;

import com.springddd.infrastructure.persistence.entity.SysUserRoleEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysUserRoleViewMapStruct {

    SysUserRoleView toView(SysUserRoleEntity entity);

    List<SysUserRoleView> toViewList(List<SysUserRoleEntity> entities);
}
