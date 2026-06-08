package com.springddd.application.service.user.dto;

import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysUserViewMapStruct {

    SysUserView toView(SysUserEntity entity);

    List<SysUserView> toViewList(List<SysUserEntity> entities);
}
